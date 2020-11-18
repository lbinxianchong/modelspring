## 控制器模板

import com.lbin.server.component.server.ComponentServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2019/4/4
 */
@Component
public class #{entity}Server extends ComponentServer<#{entity}> {

    @Autowired
    private #{entity}Service #{name}Service;

}
