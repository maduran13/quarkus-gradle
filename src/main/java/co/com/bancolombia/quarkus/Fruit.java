package co.com.bancolombia.quarkus;

import java.util.Objects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A REST entity representing a fruit.
 */
@RegisterForReflection // Lets Quarkus register this class for reflection during the native build
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Fruit {
    private String name;
    private String description;
}
