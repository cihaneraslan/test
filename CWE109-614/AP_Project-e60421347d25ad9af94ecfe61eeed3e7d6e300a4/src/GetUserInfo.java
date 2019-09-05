import java.io.ByteArrayInputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
public class GetUserInfo implements Runnable { 
   private HttpServletRequest req; 
   private HttpServletResponse resp; 
   private AsyncContext asyncCtx; 
   public GetUserInfo(HttpServletRequest req, HttpServletResponse resp, AsyncContext asyncCtx) { 
      this.req = req; 
      this.resp = resp; 
      this.asyncCtx = asyncCtx; 
   }
   @Override 
   public void run() {  
      HttpSession sess = req.getSession(); 
      OAuthService service = (OAuthService)sess.getAttribute("oauth2Service");
      //Get the all important authorization code 
      String code = req.getParameter("code"); 
      System.out.println(service);
      //Construct the access token 
      Token token = service.getAccessToken(null, new Verifier(code)); 
      //Save the token for the duration of the session 
      sess.setAttribute("token", token);
//      //Perform a proxy login 
//      try { 
//         req.login("fred", "fredfred"); 
//      } catch (ServletException e) { 
//         //Handle error - should not happen 
//      }
      //Now do something with it - get the user's G+ profile 
      OAuthRequest oReq = new OAuthRequest(Verb.GET, 
            "https://www.googleapis.com/oauth2/v2/userinfo"); 
      service.signRequest(token, oReq); 
      Response oResp = oReq.send(); 

      //Read the result 
      JsonReader reader = Json.createReader(new ByteArrayInputStream(oResp.getBody().getBytes())); 
      JsonObject profile = reader.readObject();
      System.out.println("profile: "+profile);
      //Save the user details somewhere or associate it with 
      sess.setAttribute("name", profile.getString("name")); 
      sess.setAttribute("email", profile.getString("email")); 
      asyncCtx.complete(); 
      System.out.println("GetUserInfo: "+sess.getAttribute("name") +" : "+ sess.getAttribute("email"));
   } 
}