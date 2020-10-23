package com.lbin.component.actionLog.aop;


import com.lbin.common.util.HttpServletUtil;
import com.lbin.component.actionLog.action.base.BaseAction;
import com.lbin.component.actionLog.annotation.ActionLog;
import com.lbin.component.actionLog.domain.BaseLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 行为日志注解AOP
 *
 * @author
 * @date 2018/11/12
 */
@Aspect
//@Component
@Slf4j
public class ActionLogAop {

//    private final String POINT_CUT = "execution(* com.xx.xx..*(..))";
//    @Pointcut(POINT_CUT)


    @Pointcut("@annotation(com.lbin.component.actionLog.annotation.ActionLog)")
    public void actionLog() {

    }

    ;

    @Around("actionLog()")
    public Object recordLog(ProceedingJoinPoint point) throws Throwable {
        // 先执行切入点，获取返回值
        Object proceed = point.proceed();

        /* 读取ActionLog注解消息 */
        Method method = ((MethodSignature) (point.getSignature())).getMethod();

        ActionLog anno = method.getAnnotation(ActionLog.class);
        // 获取name值
        String name = anno.name();
        // 获取message值
        String message = anno.message();
        // 获取key值
        String key = anno.key();
        // 获取行为模型
        Class<? extends BaseAction> action = anno.action();


        HttpServletRequest request = HttpServletUtil.getRequest();
        HttpServletResponse response = HttpServletUtil.getResponse();
        // 封装日志实例对象
        BaseLog baseLog = new BaseLog();
        baseLog.setIpaddr(request.getLocalAddr());
        baseLog.setEntity(point.getTarget().getClass());
        baseLog.setMethod(method);
        baseLog.setName(name);
        baseLog.setMessage(message);


        return proceed;
    }
}
