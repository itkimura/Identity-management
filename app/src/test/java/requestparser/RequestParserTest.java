package requestparser;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class RequestParserTest {
    /**
     * Test for isValidURI
     */
    @Test
    void isValidURI_WhenValidURI_ReturnTrue() {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        try {
            assertTrue(sut.isValidURI("visma-identity://login?source=severa"), "Should return true");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void isValidURI_WhenWrongURI_ReturnFalse() {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        try {
            assertFalse(sut.isValidURI("hello world"), "Should return true");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Test for isValidScheme
     */
    @Test
    void isValidScheme_WhenValidScheme_ReturnTrue() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://login?source=severa");
        assertTrue(sut.isValidScheme(uri), "Should return true");
    }

    @Test
    void isValidScheme_WhenWrongScheme_ReturnFalse() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("hive-helsinki://login?source=severa");
        assertFalse(sut.isValidScheme(uri), "Should return false");
    }


    /**
     * Test for isValidPath
     */
    @Test
    void isValidPath_WhenValidPath_ReturnTrue() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://sign?source=vismasign&documentid=105ab44");
        assertTrue(sut.isValidPath(uri), "Should return true");
    }

    @Test
    void isValidPath_WhenWrongPath_ReturnFalse() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://hivehelsinki?source=vismasign&documentid=105ab44");
        assertFalse(sut.isValidPath(uri), "Should return false");
    }


    /**
     * Test for isValidLogin
     */
    @Test
    void isValidLogin_WhenValid_ReturnTrue() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://login?source=severa");
        var query = sut.stringToParameters(uri.getQuery());
        assertTrue(sut.isValidLogin(query), "Should return true");
    }

    @Test
    void isValidLogin_WhenTooMuchInput_ReturnFalse() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://login?source=severa&paymentnumber=102226");
        var query = sut.stringToParameters(uri.getQuery());
        assertFalse(sut.isValidLogin(query), "Should return false");
    }

    @Test
    void isValidLogin_WhenIntegerforSource_ReturnFalse() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://login?source=102226");
        var query = sut.stringToParameters(uri.getQuery());
        assertFalse(sut.isValidLogin(query), "Should return false");
    }

    @Test
    void isValidLogin_WhenNoSource_ReturnFalse() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://login?documentid=105ab44");
        var query = sut.stringToParameters(uri.getQuery());
        assertFalse(sut.isValidLogin(query), "Should return false");
    }

    /**
     * Test for isValidConfirm
     */
    @Test
    void isValidConfirm_WhenValid_ReturnTrue() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://confirm?source=netvisor&paymentnumber=102226");
        var query = sut.stringToParameters(uri.getQuery());
        assertTrue(sut.isValidConfirm(query), "Should return true");
    }

    @Test
    void isValidConfirm_WhenNoPaymentNumber_ReturnFalse() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://confirm?source=netvisor&source=hive");
        var query = sut.stringToParameters(uri.getQuery());
        assertFalse(sut.isValidConfirm(query), "Should return false");
    }

    @Test
    void isValidConfirm_WhenStringForPaymentNumber_ReturnFalse() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://confirm?source=netvisor&paymentnumber=hivehelsinki");
        var query = sut.stringToParameters(uri.getQuery());
        assertFalse(sut.isValidConfirm(query), "Should return false");
    }

    /**
     * Test for isValidSign
     */
    @Test
    void isValidSign_WhenValid_ReturnTrue() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://sign?source=vismasign&documentid=105ab44");
        var query = sut.stringToParameters(uri.getQuery());
        assertTrue(sut.isValidSign(query), "Should return true");
    }

    @Test
    void isValidSign_WhenWrongInput_ReturnFalse() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://sign?source=severa&paymentnumber=102226");
        var query = sut.stringToParameters(uri.getQuery());
        assertFalse(sut.isValidSign(query), "Should return false");
    }

    @Test
    void isValidSign_WhenTooManyInput_ReturnFalse() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        URI uri = new URI("visma-identity://sign?source=vismasign&documentid=105ab44&login?source=102226");
        var query = sut.stringToParameters(uri.getQuery());
        assertFalse(sut.isValidSign(query), "Should return false");
    }

    /**
     * Test for requestParser
     */

    @Test
    void requestParser_WhenValid_ReturnTrue() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        String str = "visma-identity://sign?source=vismasign&documentid=105ab44";
        URI uri = new URI(str);
        try {
            var res = sut.requestParser(str);
            var path = res.path();
            var p1 = res.parameters().get(0);
            var p2 = res.parameters().get(1);
            System.out.println("path" + uri.getHost());
            assertTrue(path.equals(uri.getHost())
                            && p1.key().equals("source") && p1.value().equals("vismasign")
                            && p2.key().equals("documentid") && p2.value().equals("105ab44"),
                    "Should return true");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void requestParser_WhenWrong_ReturnExceptions() throws URISyntaxException {
        IdentityManagementImpl sut = new IdentityManagementImpl();
        String str = "visma-identity://sign?source=vismasign&documentid=hive";
        URI uri = new URI(str);
        Exception exception = assertThrows(Exception.class, () -> {
            sut.requestParser(str);
        });
        String correctException = "It is not a valid query parameter";
        assertTrue(exception.getMessage().equals(correctException), "Should return true");
    }
}
