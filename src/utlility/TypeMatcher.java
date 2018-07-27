package utlility;

public class TypeMatcher {
    public static boolean isInstanceOf(Object obj, Class cls) {
        return obj.getClass().equals(cls);
    }
}
