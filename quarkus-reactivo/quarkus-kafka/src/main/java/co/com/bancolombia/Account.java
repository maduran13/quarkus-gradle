package co.com.bancolombia;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A REST entity representing a legume.
 */
@RegisterForReflection // Lets Quarkus register this class for reflection during the native build
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Account {
    private String number;
    private String type;
    private String description;
}
