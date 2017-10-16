package com.project.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.project.constant.ProjectUtilityConstant;
import com.project.dto.ProjectDO;
import com.project.dto.ProjectHolder;
import com.project.dto.ResourceHolder;

public class ProjecUtilContext {
    private static ProjectHolder projectHolder;
    private static ResourceHolder resourceHolder;
    private static final  Map<String, Object> context = new HashMap<>();

    private ProjecUtilContext() {
        super();
    }

    public static final ProjectHolder getProjectHolder() {
        return projectHolder;
    }

    public static final ResourceHolder getResourceHolder() {
        return resourceHolder;
    }

    public static final Object get(String key) {
        return context.get(key);
    }

    public static final String getProperties(String key) {
        if (context.get(key) == null) {
            ResourceLoaderUtil.loadPropertirs(key);
        }
        return (String) context.get(key);
    }

    public static final Object remove(String key) {
        return context.remove(key);
    }

    public static final Object put(String key, Object value) {
        return context.put(key, value);
    }

    public static final void addProject(ProjectDO value) {
        if (projectHolder == null) {
            projectHolder = new ProjectHolder();
        }
        projectHolder.getMap().put(value.getProjectName(), value);
        saveToContext();
    }

    public static void addResource(ProjectDO newResource) {
        if (resourceHolder == null) {
            resourceHolder = new ResourceHolder();
        }
        resourceHolder.getResources().add(newResource);
        saveToContext();
    }

    public static final ProjectDO getProject(String key) {
        return projectHolder != null ? projectHolder.getMap().get(key) : null;
    }

    public static final ProjectDO getResource(String key) {
        return resourceHolder != null ? resourceHolder.getResources().stream()
            .filter(resource -> StringUtils.isEqualsIgnoreCase(key, resource.getProjectName())).findFirst().orElse(null)
            : null;

    }

    public static final void deleteProject(String projectName) {
        if (projectHolder != null) {
            String removedProject = projectHolder.getMap().remove(projectName).getProjectName();
            List<String> childProjectNames = projectHolder.getMap().values().stream()
                .filter(proj -> StringUtils.isEqualsIgnoreCase(removedProject, proj.getParentId()))
                .map(ProjectDO::getProjectName).collect(Collectors.toList());
            for (String chilProjName : childProjectNames) {
                deleteProject(chilProjName);
            }
        }
    }

    public static final void editProject(ProjectDO project) {
        ProjectDO projectRef = getProject(project.getProjectName());
        if (projectHolder != null&&projectRef!=null) {
            editModules(project.getProjectName(), project.getPath(), projectRef.getPath(),
                projectHolder.getMap());
        }
        saveToContext();
    }

    private static void editModules(String key, String updatedPath, String oldPath, Map<String, ProjectDO> map) {
        List<String> listOfModules = new ArrayList<>();
        ProjectDO projectUpdated = map.get(key);
        if (projectUpdated != null) {
            listOfModules.addAll(projectUpdated.getModules().keySet());
            projectUpdated.setPath(StringUtils.replace(projectUpdated.getPath(), oldPath, updatedPath));
            for (String name : listOfModules) {
                editModules(name, updatedPath, oldPath, projectUpdated.getModules());
                editModules(name, updatedPath, oldPath, map);

            }
        }
    }

    public static final void deleteResoursce(String key) {
        if (resourceHolder != null)
            resourceHolder.getResources().remove(getResource(key));
    }

    public static final void deleteFilters(String key) {
        if (resourceHolder != null)
            resourceHolder.getFilters().remove(key);
    }

    public static Map<String, String> getFilters() {
        return resourceHolder != null ? resourceHolder.getFilters() : null;
    }

    public static List<String> getResourceNames() {
        return resourceHolder != null
            ? resourceHolder.getResources().stream().map(ProjectDO::getProjectName).collect(Collectors.toList())
            : null;
    }

    public static void addFilter(String filterName, String filterProp) {
        if (resourceHolder == null) {
            resourceHolder = new ResourceHolder();
        }
        resourceHolder.getFilters().put(filterName, filterProp);
        saveToContext();
    }

    public static void saveToContext() {
        try {
            JAXBContext contextObj = JAXBContext.newInstance(ProjectHolder.class, ProjectDO.class);

            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshallerObj.marshal(projectHolder,
                new FileOutputStream(getProperties(ProjectUtilityConstant.RESOURCE_PATH_PROJECT)));
        }
        catch (Exception e) {
            System.out.println("unable to save project context");
        }
        try {
            JAXBContext contextObj = JAXBContext.newInstance(ResourceHolder.class, ProjectDO.class);

            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshallerObj.marshal(resourceHolder,
                new FileOutputStream(getProperties(ProjectUtilityConstant.RESOURCE_PATH_RESOURCE)));
        }
        catch (Exception e) {
            System.out.println("unable to save resource context");
        }
    }

    public static void loadContext() {
        ResourceLoaderUtil.loadPropertirs();
        try {
            JAXBContext context = JAXBContext.newInstance(ProjectHolder.class, ProjectDO.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            projectHolder = (ProjectHolder) unmarshaller
                .unmarshal(new File(getProperties(ProjectUtilityConstant.RESOURCE_PATH_PROJECT)));
        }
        catch (Exception ex) {
            System.out.println("unable to load project context");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(ResourceHolder.class, ProjectDO.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            resourceHolder = (ResourceHolder) unmarshaller
                .unmarshal(new File(getProperties(ProjectUtilityConstant.RESOURCE_PATH_RESOURCE)));
        }
        catch (Exception ex) {
            System.out.println("unable to load resource context");
        }
    }


    public static void removeFilter(String key) {
        resourceHolder.getFilters().remove(key);
        saveToContext();
    }

    public static void removeResource(String key) {
        resourceHolder.getResources().removeAll(resourceHolder.getResources().stream()
            .filter(res -> StringUtils.isEqualsIgnoreCase(key, res.getProjectName())).collect(Collectors.toList()));
        saveToContext();
    }

    public static void removeProject(String key) {
        Map<String, ProjectDO> map = projectHolder.getMap();
        ProjectDO removed = map.remove(key);
        List<String> parentIds = new ArrayList<>();
        String parentId = removed.getParentId();
        parentIds.add(parentId);
        while (parentId != null) {
            ProjectDO projectDO = projectHolder.getMap().get(parentId);
            if (projectDO != null) {
                parentId = projectDO.getParentId();
                parentIds.add(parentId);
            }
        }
        parentIds.remove(null);
        while (!parentIds.isEmpty()) {
            map = projectHolder.getMap();
            int i = parentIds.size() - 1;
            for (; i >= 0; i--) {
                String parentId2 = parentIds.get(i);
                ProjectDO projectDO = map.get(parentId2);
                System.out.println("TWO  " + parentId2);
                if (projectDO != null) {
                    map = projectDO.getModules();
                    map.remove(key);
                }
            }
            if (!parentIds.isEmpty()) {
                parentIds.remove(parentIds.size() - 1);
            }

        }
        saveToContext();
    }

}
