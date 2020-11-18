##Service层模板


import com.lbin.sql.jpa.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author
 * @date 2019/4/4
 */
@Service
public class #{entity}ServiceImpl extends BaseServiceImpl<#{entity}> implements #{entity}Service {

    @Autowired
    private #{entity}Repository #{name}Repository;
}