package layar;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;

/**
 * 
 * @author Eric Greer
 * Original code from
 *@URL http://stackoverflow.com/questions/1513603/how-to-upload-and-store-an-image-with-google-app-engine-java
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MyImage {
    /*@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
*/
    @PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String name;

    @Persistent
    Blob image;

    public MyImage() { }
    public MyImage(String name, Blob image) {
        this.setName(name); 
        this.image = image;
    }

    // JPA getters and setters and empty contructor
    // ...
    public Blob getImage(){ 
    	return image; 
    }
    
    public void setImage(Blob image){
    	this.image = image; 
    }
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param id the id to set
	 */
/*	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	/*public Long getId() {
		return id;
	}*/
}
