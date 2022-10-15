import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    private ArrayList<String> savedStrings;

    public Handler(){
        savedStrings = new ArrayList<String>();
    }

    private void add(String s){
        savedStrings.add(s);
    }

    private ArrayList<String> search(String s){
        ArrayList<String> o = new ArrayList<String>();
        for (int i = 0; i < savedStrings.size(); i++) {
            if(savedStrings.get(i).contains(s)){
                o.add(savedStrings.get(i));
            }
        }
        return o;
    }

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            if(savedStrings.size() <= 0){
                return "No strings yet!";
            }
            String allStrings = "";
            for (int i = 0; i < savedStrings.size(); i++) {
                allStrings += savedStrings.get(i) + ", ";
            }
            return String.format("All saved terms: " + allStrings.substring(0, allStrings.length() - 2));
        } else if (url.getPath().equals("/add")) {
            String[] parameters = url.getQuery().split("=");
            this.add(parameters[0]);
            return String.format("Added string " + parameters[0] + ".");
        }else if (url.getPath().equals("/search")) {
            String[] parameters = url.getQuery().split("=");
            ArrayList<String> found = this.search(parameters[0]);

            String allStrings = "";
            for (int i = 0; i < found.size(); i++) {
                allStrings += found.get(i) + ", ";
            }
            return String.format("Searched for " + parameters[0] + ". Found: " + allStrings.substring(0, allStrings.length()-2));
        } else {
            return "404 Not Found!";
        }
    }
}

class SearchServer {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
