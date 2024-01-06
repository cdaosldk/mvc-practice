import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

// @Controller 어노테이션이 설정된 모든 클래스를 출력한다
public class ReflectionTest {
    private final static Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void controllerScan() {
        Set<Class<?>> beans = getTypesAnnotatedWith(List.of(Controller.class, Service.class));

        logger.debug("beans : [{}]", beans);
    }

    @Test
    void showClass() {
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());

        logger.debug("User all declared fields : [{}]", Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));
        logger.debug("User all declared constructors : [{}]", Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toList()));
        logger.debug("User all declared methods : [{}]", Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("힙 영역에 있는 클래스 타입의 객체를 가져오는 세 가지 방법")
    void load() throws ClassNotFoundException {
        // 1) 클래스 객체를 직접 가져 오기
        Class<User> clazz = User.class;

        // 2) 객체 생성 - 객체의 getClass() 메서드 호출을 통해 가져 오기
        User user = new User("cdaosldk", "유성재");
        Class<? extends User> clazz2 = user.getClass();

        // 3) Class.forName
        Class<?> clazz3 = Class.forName("org.example.model.User");

        logger.debug("clazz : [{}]", clazz);
        logger.debug("clazz2 : [{}]", clazz2);
        logger.debug("clazz3 : [{}]", clazz3);

        assertThat(clazz == clazz2).isTrue();
        assertThat(clazz2 == clazz3).isTrue();
        assertThat(clazz3 == clazz).isTrue();
    }

    private static Set<Class<?>> getTypesAnnotatedWith(List<Class<? extends Annotation>> annotations) {
        // 이 패키지 밑에 있는 모든 클래스 대상을 리플렉션 한다
        Reflections reflections = new Reflections("org.example");

        Set<Class<?>> beans = new HashSet<>();

        annotations.forEach(annotation -> beans.addAll(reflections.getTypesAnnotatedWith(annotation)));
        
        // controller 어노테이션이 붙어있는 모든 클래스를 beans에 추가한다는 의미
//        beans.addAll(reflections.getTypesAnnotatedWith(Controller.class));
//        beans.addAll(reflections.getTypesAnnotatedWith(Service.class));

        return beans;
    }
}
