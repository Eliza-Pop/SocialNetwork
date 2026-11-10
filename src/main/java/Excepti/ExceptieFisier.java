package Excepti;

public class ExceptieFisier extends RuntimeException {

  public ExceptieFisier(String message) {
    super(message);
  }

  public ExceptieFisier(String message,Throwable e) {
    super(message,e);
  }
}
