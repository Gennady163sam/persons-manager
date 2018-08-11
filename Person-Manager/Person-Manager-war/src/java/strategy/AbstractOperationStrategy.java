package strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import orm.entity.Person;

public interface AbstractOperationStrategy {
    public abstract void saveInDataBase(Person person, HttpServletRequest request, HttpServletResponse response);
}
