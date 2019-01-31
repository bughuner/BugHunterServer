package bughunter.bughunterserver.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author sean
 * @date 2019-01-22.
 */
@Entity(name = "node")
public class Node {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "app_key")
    private String appKey;

    @Column(name = "window")
    private String window;

    /**
     * ACT
     * DIALOG
     * Launcher
     * */
    @Column(name = "type")
    private String type;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
