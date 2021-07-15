package com.lbin.modelspring.aop;


import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

@Aspect
@Slf4j
public class ApiAop {

    //��ʾƥ�����з���
    private final String POINT_CUT1 = "execution(* *(..))";
    //��ʾƥ��com.api.server.UserService�����еĹ��з���
    private final String POINT_CUT2 = "execution(public * com.api.service.UserService.*(..))";
    //��ʾƥ��com.api.server�������Ӱ��µ����з���
    private final String POINT_CUT3 = "execution(* com.api.server..*.*(..))";

    @Pointcut(POINT_CUT1)
    private void pointcut(){}

    /**
     * ǰ��֪ͨ����������ǰ������
     * @param joinPoint
     */
    @Before(value = POINT_CUT1)
    public void before(JoinPoint joinPoint){
        log.info("ǰ��֪ͨ");
        //��ȡĿ�귽���Ĳ�����Ϣ
        Object[] obj = joinPoint.getArgs();
        //AOP���������Ϣ
        joinPoint.getThis();
        //�����Ŀ�����
        joinPoint.getTarget();
        //�õ���� ֪ͨ��ǩ��
        Signature signature = joinPoint.getSignature();
        //���������һ������
        log.info("���������һ������"+signature.getName());
        //AOP�����������
        log.info("AOP�����������"+signature.getDeclaringTypeName());
        //AOP��������ࣨclass����Ϣ
        signature.getDeclaringType();
        //��ȡRequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //�ӻ�ȡRequestAttributes�л�ȡHttpServletRequest����Ϣ
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        //���Ҫ��ȡSession��Ϣ�Ļ�����������д��
        //HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        //��ȡ�������
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String,String> parameterMap = Maps.newHashMap();
        while (enumeration.hasMoreElements()){
            String parameter = enumeration.nextElement();
            parameterMap.put(parameter,request.getParameter(parameter));
        }
        String str = JSONUtil.toJsonStr(parameterMap);
        if(obj.length > 0) {
            log.info("����Ĳ�����ϢΪ��"+str);
        }
    }

    /**
     * **ע�⣺�����õ���JoinPoint��RequestContextHolder��
     * 1����ͨ��JoinPoint���Ի��֪ͨ��ǩ����Ϣ����Ŀ�귽������Ŀ�귽��������Ϣ�ȡ�
     * 2����ͨ��RequestContextHolder����ȡ������Ϣ��Session��Ϣ��**
     */

    /**
     * ���÷���֪ͨ
     * ������Ҫע�����:
     *      ��������еĵ�һ������ΪJoinPoint����ڶ�������Ϊ����ֵ����Ϣ
     *      ��������еĵ�һ��������ΪJoinPoint�����һ������Ϊreturning�ж�Ӧ�Ĳ���
     * returning���޶���ֻ��Ŀ�귽������ֵ��֪ͨ������Ӧ��������ʱ����ִ�к��÷���֪ͨ������ִ�У�
     *            ����returning��Ӧ��֪ͨ��������ΪObject���ͽ�ƥ���κ�Ŀ�귵��ֵ
     * @param joinPoint
     * @param keys
     */
    @AfterReturning(value = POINT_CUT1,returning = "keys")
    public void doAfterReturningAdvice1(JoinPoint joinPoint,Object keys){
        log.info("��һ�����÷���֪ͨ�ķ���ֵ��"+keys);
    }

    @AfterReturning(value = POINT_CUT1,returning = "keys",argNames = "keys")
    public void doAfterReturningAdvice2(String keys){
        log.info("�ڶ������÷���֪ͨ�ķ���ֵ��"+keys);
    }

    /**
     * �����쳣֪ͨ
     *  ����һ�����֣�����������ƥ��֪ͨʵ�ַ�����һ������������Ŀ�귽���׳��쳣���غ󣬽���Ŀ�귽���׳����쳣����֪ͨ������
     *  throwing:�޶���ֻ��Ŀ�귽���׳����쳣��֪ͨ������Ӧ�����쳣����ʱ����ִ�к����쳣֪ͨ������ִ�У�
     *            ����throwing��Ӧ��֪ͨ��������ΪThrowable���ͽ�ƥ���κ��쳣��
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = POINT_CUT1,throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint,Throwable exception){
        //Ŀ�귽������
        log.info(joinPoint.getSignature().getName());
        if(exception instanceof NullPointerException){
            log.info("�����˿�ָ���쳣!!!!!");
        }
    }

    /**
     * ��������֪ͨ��Ŀ�귽��ֻҪִ�����˾ͻ�ִ�к���֪ͨ������
     * @param joinPoint
     */
    @After(value = POINT_CUT1)
    public void doAfterAdvice(JoinPoint joinPoint){
        log.info("��������ִ֪ͨ����!!!!");
    }

    /**
     * ����֪ͨ��
     *   ����֪ͨ�ǳ�ǿ�󣬿��Ծ���Ŀ�귽���Ƿ�ִ�У�ʲôʱ��ִ�У�ִ��ʱ�Ƿ���Ҫ�滻����������ִ������Ƿ���Ҫ�滻����ֵ��
     *   ����֪ͨ��һ������������org.aspectj.lang.ProceedingJoinPoint����
     */
    @Around(value = POINT_CUT1)
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
        log.info("����֪ͨ��Ŀ�귽������"+proceedingJoinPoint.getSignature().getName());
        try {
            Object obj = proceedingJoinPoint.proceed();
            return obj;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
