package stuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@ManagedBean
@ViewScoped
public class Tester implements Serializable
{
	private static final long serialVersionUID = 7264969006393638542L;
	String user;
	boolean checked;
	boolean verified;
	String url;

	@ManagedProperty(value="#{master}")
    private Master master;

	@PostConstruct
	public void initialise()
	{
		user = "";
		checked = false;
		verified = false;
		url = "";
	}

	public String getUrl() {
		isVerified();//to make sure it gets the url
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Master getMaster() {
		return master;
	}

	public void setMaster(Master master) {
		this.master = master;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public boolean isVerified()
	{
		if(!checked)
		{
			verified = master.isVerified(user);
			JSONObject obj = null;
			try {
				obj = jsonFromURL("https://en.gravatar.com/" + user + ".json");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(obj != null)
			{
				JSONArray arr = obj.getJSONArray("entry");
				JSONObject obj2 = (JSONObject) arr.get(0);
				url = obj2.getString("thumbnailUrl");
			}
		}
		checked = true;
		return verified;
	}

	public int getSize()
	{
		return master.getSize();
	}

	  //This method gets the URL data as an input stream, then parses the data as a JSON.
	  public static JSONObject jsonFromURL(String url) throws IOException, JSONException {
	    InputStream input = new URL(url).openStream();
	    try {
	      InputStreamReader isr=
	              new InputStreamReader(input, Charset.forName("UTF-8"));
	      BufferedReader br = new BufferedReader(isr);
	      String jsonString = readerToString(br);
	      JSONObject jsonObj = new JSONObject(jsonString);
	      return jsonObj;
	    } finally {
	      input.close();
	    }
	  }
	    //Parses the received Reader object to a String representation.
	   private static String readerToString(Reader reader) throws IOException {
	    StringBuilder stringBuilder = new StringBuilder();

	    int character;
	    while ((character = reader.read())!=-1) {
	      stringBuilder.append((char) character);
	    }
	    return stringBuilder.toString();
	  }
}
