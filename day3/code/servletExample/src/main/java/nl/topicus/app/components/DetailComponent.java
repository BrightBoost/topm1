package nl.topicus.app.components;

import nl.topicus.framework.Component;

/**
 * DetailComponent - displays detailed information about a choice
 */
public class DetailComponent extends Component {

    private String title;
    private String emoji;
    private String description;
    private String[] details;

    public DetailComponent(String id) {
        super(id);
    }

    public void setContent(String title, String emoji, String description, String... details) {
        this.title = title;
        this.emoji = emoji;
        this.description = description;
        this.details = details;
    }

    @Override
    public String renderHtml() {
        StringBuilder html = new StringBuilder();

        html.append("<div id='").append(id).append("' class='detail-component'>\n");
        html.append("  <div class='detail-header'>\n");
        html.append("    <div class='detail-emoji'>").append(emoji).append("</div>\n");
        html.append("    <h2>").append(title).append("</h2>\n");
        html.append("  </div>\n");
        html.append("  <p class='detail-description'>").append(description).append("</p>\n");

        if (details != null && details.length > 0) {
            html.append("  <div class='detail-list'>\n");
            html.append("    <h3>Details:</h3>\n");
            html.append("    <ul>\n");
            for (String detail : details) {
                html.append("      <li>").append(detail).append("</li>\n");
            }
            html.append("    </ul>\n");
            html.append("  </div>\n");
        }

        html.append("  <button onclick='goBack()' class='back-button'>← Terug naar Keuzes</button>\n");
        html.append("</div>\n");

        return html.toString();
    }
}