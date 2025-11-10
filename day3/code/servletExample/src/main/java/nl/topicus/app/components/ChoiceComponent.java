package nl.topicus.app.components;

import nl.topicus.framework.Component;
import java.util.List;
import java.util.ArrayList;

/**
 * ChoiceComponent - displays a list of choices as cards
 *
 * This is a reusable component that can be used anywhere.
 * Wicket Ultra Light style!
 */
public class ChoiceComponent extends Component {

    private String title;
    private List<Choice> choices;

    public ChoiceComponent(String id, String title) {
        super(id);
        this.title = title;
        this.choices = new ArrayList<>();
    }

    /**
     * Add a choice to this component
     */
    public void addChoice(String id, String name, String emoji, String description) {
        choices.add(new Choice(id, name, emoji, description));
    }

    @Override
    public String renderHtml() {
        StringBuilder html = new StringBuilder();

        html.append("<div id='").append(id).append("' class='choice-component'>\n");
        html.append("  <h2>").append(title).append("</h2>\n");
        html.append("  <div class='choices-grid'>\n");

        for (Choice choice : choices) {
            html.append("    <div class='choice-card' onclick='selectChoice(\"")
                    .append(choice.id).append("\")'>\n");
            html.append("      <div class='choice-emoji'>").append(choice.emoji).append("</div>\n");
            html.append("      <h3>").append(choice.name).append("</h3>\n");
            html.append("      <p>").append(choice.description).append("</p>\n");
            html.append("    </div>\n");
        }

        html.append("  </div>\n");
        html.append("</div>\n");

        return html.toString();
    }

    /**
     * Inner class to hold choice data
     */
    public static class Choice implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        public String id;
        public String name;
        public String emoji;
        public String description;

        public Choice(String id, String name, String emoji, String description) {
            this.id = id;
            this.name = name;
            this.emoji = emoji;
            this.description = description;
        }
    }

    public List<Choice> getChoices() {
        return choices;
    }
}
