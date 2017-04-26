package by.korovkin.restClient.entity;

public class Issue implements Entity {

    private long id;
    private String summary;
    private Reference priority;
    private User user;
    private Reference type;
    private Reference status;
    private Project project;

    public Issue() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Reference getPriority() {
        return priority;
    }

    public void setPriority(Reference priority) {
        this.priority = priority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reference getType() {
        return type;
    }

    public void setType(Reference type) {
        this.type = type;
    }

    public Reference getStatus() {
        return status;
    }

    public void setStatus(Reference status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", summary='" + summary + '\'' +
                ", priority=" + priority +
                ", user=" + user +
                ", type=" + type +
                ", status=" + status +
                ", project=" + project +
                '}';
    }
}
