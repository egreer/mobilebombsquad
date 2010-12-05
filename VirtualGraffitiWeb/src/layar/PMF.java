package layar;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/** Persistence Manager Factory Singleton 
 * 
 * @author Eric Greer
 * @author Andrew Yee
 *
 */
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
