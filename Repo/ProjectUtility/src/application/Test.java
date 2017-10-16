package application;

import com.project.util.StringUtils;

public class Test {
    public static void main(String[] args) {

        String str = "012345";
        System.out.println(StringUtils.substring(str, 0, 7) + "::::" + str.substring(0, 6));

        /*
        try {
        	JAXBContext contextObj = JAXBContext.newInstance(ProjectDO.class);
        
        	Marshaller marshallerObj = contextObj.createMarshaller();
        	marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        	ProjectDO resourceHolder = new ProjectDO();
        	resourceHolder.setProjectName("LL");
        	resourceHolder.setPath("KK");
        	resourceHolder.setParentId("lKK");
        	System.out.println(resourceHolder.getProjectName());
        	System.out.println(resourceHolder.getPath());
        	marshallerObj.marshal(resourceHolder, new FileOutputStream("test.xml"));
        } catch (Exception e) {
        	System.out.println("not able to save resource context");
        }
        try {
        	JAXBContext context = JAXBContext.newInstance (ProjectDO.class);
        	Unmarshaller unmarshaller = context.createUnmarshaller();
        	ProjectDO resourceHolder = (ProjectDO) unmarshaller
        			.unmarshal(new File("test.xml"));
        	System.out.println(resourceHolder.getProjectName());
        	System.out.println(resourceHolder.getPath());
        	System.out.println(resourceHolder.getParentId());
        } catch (Exception ex) {
        	System.out.println("unable to load resource context");
        }
        */}
}
