package requestparser;

import com.google.common.base.Splitter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IdentityManagementImpl implements IdentityManagement{

    public PathAndParameters identityManagement(String uriStr) throws Exception {
        if (isValidURI(uriStr) == false)
            throw new Exception("It is not a valid URI");
        URI uri = new URI(uriStr);
        if (isValidScheme(uri) == false)
            throw new Exception("It is not a valid scheme");
        if (isValidPath(uri) == false)
            throw new Exception("It is not a valid path");
        PathAndParameters output = new PathAndParameters(uri.getHost(), stringToParameters(uri.getQuery()));
        if (isValidParameter(output) == false)
            throw new Exception("It is not a valid query parameter");
        return output;
    }

    /**
     * validation:
     * isValidURI       ->  Validation URI format
     * isValidScheme    ->  Used URI scheme is right: visma-identity
     * isValidPath      ->  Path is one of the allowed: login, confirm or sign
     */
    boolean isValidURI(String str) {
        try {
            URI uri = new URI(str);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    boolean isValidScheme(URI uri) {
        var uriScheme = uri.getScheme();
        if (uriScheme.equals("visma-identity"))
            return true;
        else
            return false;
    }

    boolean isValidPath(URI uri) {
        var uriPath = uri.getHost();
        if (uriPath.equals("login") || uriPath.equals("confirm") || uriPath.equals("sign"))
            return true;
        else
            return false;
    }

    Map<String, String> queryToMap(String str) {
        Map<String, String> map = Splitter.on("&")
                .withKeyValueSeparator("=")
                .split(str);
        return map;
    }

    List<Parameter> stringToParameters(String str) {
        String[] query = str.split("&", -2);
        List<Parameter> parameters = new ArrayList<>();
        for (int i = 0; i < query.length; i++) {
            String[] pair = query[i].split("=", 2);
            parameters.add(new Parameter(pair[0], pair[1]));
        }
        return parameters;
    }

    boolean isDigit(String str) {
        try {
            int number = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }


    /**
     * Query tests
     * <p>
     * documentid(type:string)
     * payment number(type:integer)
     * source(type:string)
     */
    boolean isValidDocumentID(Parameter p) {
        if (p.key().equals("documentid") && isDigit(p.value()) == false)
            return true;
        else
            return false;
    }

    boolean isValidSource(Parameter p) {
        if (p.key().equals("source") && isDigit(p.value()) == false)
            return true;
        else
            return false;
    }

    boolean isValidPaymentNumber(Parameter p) {
        if (p.key().equals("paymentnumber") && isDigit(p.value()) == true)
            return true;
        else
            return false;
    }

    /**
     * Path login:
     * source(type:string)
     * Example: visma-identity://login?source=severa
     */
    boolean isValidLogin(List<Parameter> query) {
        if (query.size() == 1 && isValidSource(query.get(0)))
            return true;
        else
            return false;
    }

    /**
     * Path confirm:
     * source(type:string)
     * payment number(type:integer)
     * Example: visma-identity://confirm?source=netvisor&paymentnumber=102226
     */
    boolean isValidConfirm(List<Parameter> query) {
        if (query.size() != 2)
            return false;
        var p1 = query.get(0);
        var p2 = query.get(1);
        if ((isValidSource(p1) && isValidPaymentNumber(p2)) || (isValidSource(p2) && isValidPaymentNumber(p1)))
            return true;
        return false;
    }

    /**
     * Path sign:
     * source(type: string)
     * documentid(type:string)
     * Example: visma-identity://sign?source=vismasign&documentid=105ab44
     * Example: visma-identity://sign?documentid=105ab44&source=vismasign
     */
    boolean isValidSign(List<Parameter> query) {
        if (query.size() != 2)
            return false;
        var p1 = query.get(0);
        var p2 = query.get(1);
        if ((isValidSource(p1) && isValidDocumentID(p2)) || (isValidSource(p2) && isValidDocumentID(p1)))
            return true;
        return false;
    }

    boolean isValidParameter(PathAndParameters input) {
        if (input.path().equals("login"))
            return (isValidLogin(input.parameters()));
        else if (input.path().equals("confirm"))
            return (isValidConfirm(input.parameters()));
        else if (input.path().equals("sign"))
            return (isValidSign(input.parameters()));
        return false;
    }

}
