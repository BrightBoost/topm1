package nl.topicus.framework;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Component class - inspired by Wicket's Component
 *
 * Components are reusable UI elements that:
 * - Have their own state
 * - Can render themselves as HTML
 * - Can be updated via AJAX
 */
public abstract class Component implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String id;
    protected Map<String, Object> data;

    public Component(String id) {
        this.id = id;
        this.data = new HashMap<>();
    }

    /**
     * Render component as HTML
     */
    public abstract String renderHtml();

    /**
     * Get component data as Map (for JSON serialization)
     */
    public Map<String, Object> getData() {
        Map<String, Object> result = new HashMap<>(data);
        result.put("id", id);
        result.put("type", this.getClass().getSimpleName());
        return result;
    }

    /**
     * Set data on this component
     */
    public void setData(String key, Object value) {
        this.data.put(key, value);
    }

    /**
     * Get data from this component
     */
    public Object getData(String key) {
        return this.data.get(key);
    }

    public String getId() {
        return id;
    }
}
