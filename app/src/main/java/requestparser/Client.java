package requestparser;

public class Client {

    IdentityManagement identityManagement;
    private final String source;
    Client(String source){
        this.source = source;
        this.identityManagement = new IdentityManagementImpl();
    }

    boolean login() {
        var uri = "visma-identity://login?source=" + this.source;
        try{
            this.identityManagement.identityManagement(uri);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    boolean confirm(int paymentNumber) throws Exception {
        var uri = "visma-identity://confirm?source=" + this.source + "&paymentnumber=" + paymentNumber;
        try{
            this.identityManagement.identityManagement(uri);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    boolean sign(String documentId) throws Exception {
        var uri = "visma-identity://sign?source=" + this.source + "&documentid=" + documentId;
        try{
            this.identityManagement.identityManagement(uri);
            return true;
        }
        catch (Exception e){
            return false;
        }

    }
}
