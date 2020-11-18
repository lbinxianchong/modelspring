## 控制器模板

import com.lbin.server.component.request.ComponentRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 * @date 2019/4/4
 */
@Controller
@RequestMapping("#{requestMapping}")
public class #{entity}Controller extends ModelRequest<#{entity}, #{entity}Valid> {

    @Autowired
    private #{entity}Repository #{name}Repository;

}
