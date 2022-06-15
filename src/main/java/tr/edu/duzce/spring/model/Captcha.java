package tr.edu.duzce.spring.model;

import org.hibernate.type.descriptor.sql.LobTypeMappings;

import javax.persistence.*;
import java.awt.*;
import java.sql.Blob;
import java.util.Arrays;

/**
 * @author mahmutcandurak
 */

@Entity
@Table(name = "captcha_table", schema = "captcha")
public class Captcha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "captcha_id")
    Long Id;

    String description;

    @Lob()
    @Column(columnDefinition = "BLOB")
    byte[] captcha;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCaptcha() {
        return captcha;
    }

    public void setCaptcha(byte[] captcha) {
        this.captcha = captcha;
    }

    @Override
    public String toString() {
        return "Captcha{" +
                "Id=" + Id +
                ", description='" + description + '\'' +
                ", captcha=" + Arrays.toString(captcha) +
                '}';
    }
}
