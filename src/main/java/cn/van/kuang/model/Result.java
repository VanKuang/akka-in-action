package cn.van.kuang.model;

public class Result {

    private int id;
    private String name;

    public Result(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
