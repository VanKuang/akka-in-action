package cn.van.kuang.akka.model;

public class Result {

    private final int id;
    private final String name;

    public Result(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
