import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;

public class ProtoToJson(){

//msg is any protobuf object 

public static String generateJsonFromMsg(GeneratedMessage msg) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException{
		Map<FieldDescriptor,Object> allFields = msg.getAllFields();
		StringBuilder sb = new StringBuilder();
		for(FieldDescriptor field: allFields.keySet()){
			sb.append("\"" + field.getName() +"\"");
			sb.append(":");
			boolean isRepeated = field.isRepeated();
			Class<?> c = Class.forName(msg.getClass().getName());
			try{
				String name = field.getName();
				if(isRepeated){
					name = name + "List";
				}
				Method method = c.getDeclaredMethod ("get"+ WordUtils.capitalize(name,new char [] {'_'}).replace("_", "") );
				Object value = method.invoke(msg);
				if(value instanceof GeneratedMessage){
					if(!isRepeated)
						 sb.append("{");
					sb.append(generateJsonFromMsg((GeneratedMessage)value));
					if(!isRepeated)
						 sb.append("}");
				}else {
					if(isRepeated)
						sb.append(value);
					else
						sb.append("\"" + value + "\"");
				}
			}catch(Exception e){
				System.out.println(e);
			}
			
			sb.append(",");
		}
		return sb.deleteCharAt(sb.length() -1).toString();
		
	}

}
