package requestparser;

import java.util.List;

public record PathAndParameters (String path,
                                List<Parameter> parameters) {
}
