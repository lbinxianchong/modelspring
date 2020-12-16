##dao层模板
import com.lbin.sql.jpa.repository.BaseRepository;

/**
 * @author
 * @date 2019/4/4
 */
public interface #{entity}Repository extends BaseRepository<#{entity}, Long> {
}