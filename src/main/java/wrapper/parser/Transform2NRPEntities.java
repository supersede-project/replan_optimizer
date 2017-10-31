package wrapper.parser;

import entities.Employee;
import entities.Feature;
import entities.PriorityLevel;
import entities.Skill;
import io.swagger.model.Resource;
import logic.NextReleaseProblem;
import logic.PlanningSolution;

import java.util.ArrayList;
import java.util.List;

public class Transform2NRPEntities {

    /*Employee-Resources*/
    public Employee Resource2Employee(Resource resource) {

        List<Skill> skills = SkillList2Entities(resource.getSkills());

        return new Employee(resource.getName(),resource.getAvailability(),skills);

    }

    public List<Employee> ListResource2Employee( List<Resource> resources) {
        List<Employee> employees = new ArrayList<>();
        for(Resource r: resources) {
            employees.add(Resource2Employee(r));
        }
        return employees;
    }

    /*Skills*/
    public Skill Skill2Entities(io.swagger.model.Skill skill) {
        return  new Skill(skill.getName());
    }

    public List<Skill> SkillList2Entities(List<io.swagger.model.Skill> listSkills) {
        List<Skill> list = new ArrayList<>();

        for(io.swagger.model.Skill skill: listSkills) {
            list.add(Skill2Entities(skill));
        }
        return list;
    }

    /*Prioriy Level*/
    public PriorityLevel PriorityLevel2Entities(io.swagger.model.Priority priority){

        switch (priority.getLevel()) {
            case 1:
                return PriorityLevel.ONE;
            case 2:
                return PriorityLevel.TWO;
            case 3:
                return PriorityLevel.THREE;
            case 4:
                return PriorityLevel.FOUR;
            case 5:
                return PriorityLevel.FIVE;
        }

        return PriorityLevel.FIVE;
    }

    /*Feature*/
    public Feature Feature2Entities(io.swagger.model.Feature f) {
       // String name, PriorityLevel priority, Double duration, List<Feature> previousFeatures, List< Skill > requiredSkills
        return new Feature(f.getName(),
                PriorityLevel2Entities(f.getPriority()),
                f.getDuration(),
                FeatureList2Entities(f.getDepends_on()),
                SkillList2Entities(f.getRequired_skills()));
    }

    public List<Feature> FeatureList2Entities(List<io.swagger.model.Feature> listf) {
        List<Feature> featureList = new ArrayList<>();
        for (io.swagger.model.Feature f: listf){
            featureList.add(Feature2Entities(f));
        }
        return featureList;
    }

    public PlanningSolution planningSolutionToEntity(io.swagger.model.PlanningSolution swaggerSolution, NextReleaseProblem problem) {
        List<entities.PlannedFeature> plannedFeatures = new ArrayList<>();
        for (io.swagger.model.PlannedFeature pf : swaggerSolution.getJobs()) {
            entities.PlannedFeature newPf = new entities.PlannedFeature(
                    Feature2Entities(pf.getFeature()),
                    Resource2Employee(pf.getResource())
            );
            newPf.setBeginHour(pf.getBeginHour());
            newPf.setEndHour(pf.getEndHour());
            newPf.setFrozen(pf.isFrozen());

            plannedFeatures.add(newPf);
        }

        return new PlanningSolution(problem, plannedFeatures);
    }
}
