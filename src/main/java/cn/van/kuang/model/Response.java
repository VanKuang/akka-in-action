package cn.van.kuang.model;

public class Response<T> {

    private final int status;
    private final T result;

    public Response(int status, T result) {
        this.status = status;
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status + ", " +
                ((result instanceof Exception) ?
                        "exception=\'" + ((Exception) result).getMessage()
                        : "result='" + result) + '\'' +
                '}';
    }
}
