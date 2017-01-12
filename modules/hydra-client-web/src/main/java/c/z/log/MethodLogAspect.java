package c.z.log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MethodLogAspect {

    private static final org.apache.commons.logging.Log logger = LogFactory.getLog(MethodLogAspect.class);

    private static String[] types = { "java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Long",
            "java.lang.Short", "java.lang.Byte", "java.lang.Boolean", "java.lang.Char", "java.lang.String", "int",
            "double", "long", "short", "byte", "boolean", "char", "float" };

    private static final ThreadLocal<InvokeContext/* 当前线程调用时的初始时间 */> tl = new ThreadLocal<InvokeContext>();

    @After(value = "@annotation(c.z.log.MethodLog)")
    public void afterInvoke(JoinPoint joinPoint) {
            InvokeContext context=tl.get();
            logger.info(String.format("sessionid=%s afterinvoke log elapsed_second=%s",
                    context.getSessionId(),(System.currentTimeMillis()-context.getInvokeTime())/1000));
            tl.remove();
    }

    @Before(value = "@annotation(c.z.log.MethodLog)")
    public void beforeInvoke(JoinPoint joinPoint) throws Throwable {
        String[] paramNames = getFieldsName(joinPoint);
        String logContent = writeLogInfo(paramNames, joinPoint);
        Long sessionId=System.currentTimeMillis();
        logger.info(String.format("sessionid=%s,beforeinvoke log clazzName:%s, methodName: %s, param:%s",sessionId, joinPoint.getTarget().getClass()
                .getName(), joinPoint.getSignature().getName(), logContent));
        tl.set(new InvokeContext(sessionId,System.currentTimeMillis()));
    }

    private Method getMethod(JoinPoint joinPoint) {
        Method m = null;
        return null;
    }

    private String writeLogInfo(String[] paramNames, JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();

        for (int k = 0; k < args.length; k++) {
            boolean clazzFlag = true;
            Object arg = args[k];
            sb.append(paramNames[k] + " ");
            // 获取对象类型
            // String typeName = arg.getClass().getTypeName();
            String typeName = arg.getClass().getName();
            for (String t : types) {
                if (t.equals(typeName)) {
                    sb.append("=" + arg + "; ");
                    clazzFlag = false;
                }
            }
            if (clazzFlag) {
                sb.append(getFieldsValue(arg));
            }
        }
        return sb.toString();
    }

    /**
     * 得到方法参数的名称
     * 
     * @param cls
     * @param clazzName
     * @param methodName
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws NotFoundException
     */
    private static String[] getFieldsName(JoinPoint joinPoint) throws NoSuchMethodException, SecurityException,
            IOException {

        Class clazz = joinPoint.getTarget().getClass();
        Object[] args = joinPoint.getArgs(); // 参数列表

        Class[] parametersTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parametersTypes[i] = args[i].getClass();

        }

        Method method = clazz.getDeclaredMethod(joinPoint.getSignature().getName(), parametersTypes);

     //   String clazzName = joinPoint.getTarget().getClass().getName();
       // Signature sig = joinPoint.getSignature();
       // Class detype = sig.getDeclaringType();
     //   String methodName = joinPoint.getSignature().getName();

        String[] paramNames = MethodParamNamesScanner.methodParamNames(clazz, method);
        // getFieldsName(this.getClass(), clazzName, methodName);

        return paramNames;
    }

    /*
     * private void getParamterName(String clazzName, String methodName) throws
     * NotFoundException { ClassPool pool = ClassPool.getDefault();
     * ClassClassPath classPath = new ClassClassPath(this.getClass());
     * pool.insertClassPath(classPath);
     * 
     * CtClass cc = pool.get(clazzName); CtMethod cm =
     * cc.getDeclaredMethod(methodName); MethodInfo methodInfo =
     * cm.getMethodInfo(); CodeAttribute codeAttribute =
     * methodInfo.getCodeAttribute(); LocalVariableAttribute attr =
     * (LocalVariableAttribute) codeAttribute
     * .getAttribute(LocalVariableAttribute.tag); if (attr == null) { //
     * exception } String[] paramNames = new
     * String[cm.getParameterTypes().length]; int pos =
     * Modifier.isStatic(cm.getModifiers()) ? 0 : 1; for (int i = 0; i <
     * paramNames.length; i++) { paramNames[i] = attr.variableName(i + pos); }
     * // paramNames即参数名 for (int i = 0; i < paramNames.length; i++) {
     * System.out.println(paramNames[i]); } }
     */
    /**
     * 得到参数的值
     * 
     * @param obj
     */
    private String getFieldsValue(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        // String typeName = obj.getClass().getTypeName();
        String typeName = obj.getClass().getName();
        for (String t : types) {
            if (t.equals(typeName))
                return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("【");
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                for (String str : types) {
                    if (f.getType().getName().equals(str)) {
                        sb.append(f.getName() + " = " + f.get(obj) + "; ");
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.append("】");
        return sb.toString();
    }

}
