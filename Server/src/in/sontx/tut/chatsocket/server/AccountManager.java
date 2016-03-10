package in.sontx.tut.chatsocket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sun.istack.internal.NotNull;

import in.sontx.tut.chatsocket.bean.AccountInfo;
import in.sontx.tut.chatsocket.utils.Security;
import in.sontx.tut.chatsocket.utils.StreamUtilities;

public final class AccountManager {
	private static final String USERDAT_FILENAME = "userdat.io";
	private static AccountManager instance = null;

	public static AccountManager getInstance() {
		return instance;
	}

	public static void createInstance(String userdatDir) {
		if (!userdatDir.endsWith("/"))
			userdatDir = userdatDir + "/";
		instance = new AccountManager(userdatDir + USERDAT_FILENAME);
	}

	private static final String INIT_STATUS = "Hi everyone!";
	private final String userdatPath;

	AccountManager(String userdatPath) {
		this.userdatPath = userdatPath;
		initialize();
	}

	private void initialize() {
		if (!new File(userdatPath).exists()) {
			try {
				JSONArray jsonArray = new JSONArray("[]");
				updateToFile(jsonArray);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public AccountInfo getAccountInfo(@NotNull String username, @NotNull String passhash) {
		try {
			JSONTokener jsonTokener = new JSONTokener(readAllString());
			JSONArray jsonArray = new JSONArray(jsonTokener);
			int countOfObject = jsonArray.length();
			for (int i = 0; i < countOfObject; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject.getString("username").equals(username)
						&& jsonObject.getString("passhash").equals(passhash)) {
					AccountInfo accountInfo = new AccountInfo();
					accountInfo.setAccountId(jsonObject.getInt("id"));
					accountInfo.setDisplayName(jsonObject.getString("dispname"));
					accountInfo.setStatus(jsonObject.getString("status"));
					accountInfo.setState(AccountInfo.STATE_ONLINE);
					return accountInfo;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@NotNull
	public List<AccountInfo> getAllAccountInfos() {
		List<AccountInfo> allFriends = new ArrayList<>();
		try {
			JSONTokener jsonTokener = new JSONTokener(readAllString());
			JSONArray jsonArray = new JSONArray(jsonTokener);
			int countOfObject = jsonArray.length();
			for (int i = 0; i < countOfObject; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				AccountInfo accountInfo = new AccountInfo();
				accountInfo.setAccountId(jsonObject.getInt("id"));
				accountInfo.setDisplayName(jsonObject.getString("dispname"));
				accountInfo.setStatus(jsonObject.getString("status"));
				accountInfo.setState(AccountInfo.STATE_OFFLINE);
				allFriends.add(accountInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return allFriends;
	}

	private synchronized void updateToFile(JSONArray jsonArray) {
		OutputStream out = null;
		StringWriter writer = new StringWriter();
		File userdatFile = new File(userdatPath);
		userdatFile.delete();
		try {
			jsonArray.write(writer);
			out = new FileOutputStream(userdatFile);
			out.write(writer.toString().getBytes(Charset.forName("UTF-8")));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		} finally {
			StreamUtilities.tryCloseStream(out);
			StreamUtilities.tryCloseStream(writer);
		}
	}

	private synchronized String readAllString() {
		File userdatFile = new File(userdatPath);
		if (!userdatFile.exists())
			try {
				userdatFile.createNewFile();
			} catch (IOException e1) {
			}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(userdatFile));
			char[] buffer = new char[1024 * 32];
			int length = reader.read(buffer);
			if (length > 0)
				return new String(buffer, 0, length);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			StreamUtilities.tryCloseStream(reader);
		}
		return "";
	}

	private boolean checkExistsAccount(String username) {
		try {
			JSONTokener jsonTokener = new JSONTokener(readAllString());
			JSONArray jsonArray = new JSONArray(jsonTokener);
			int countOfObject = jsonArray.length();
			for (int i = 0; i < countOfObject; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject.getString("username").equals(username)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void addAccount(@NotNull String username, @NotNull String passhash, @NotNull String dispname) {
		if (checkExistsAccount(username))
			return;
		try {
			JSONTokener jsonTokener = new JSONTokener(readAllString());
			JSONArray jsonArray = new JSONArray(jsonTokener);
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("id", Security.getRandomInteger());
			jsonObject.put("username", username);
			jsonObject.put("passhash", passhash);
			jsonObject.put("dispname", dispname);
			jsonObject.put("status", INIT_STATUS);

			jsonArray.put(jsonObject);

			updateToFile(jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void changeAccountInfo(int id, @NotNull String key, @NotNull String value) {
		try {
			JSONTokener jsonTokener = new JSONTokener(readAllString());
			JSONArray jsonArray = new JSONArray(jsonTokener);

			int countOfAccount = jsonArray.length();
			for (int i = 0; i < countOfAccount; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject.getInt("id") == id) {
					jsonObject.put(key, value);
					break;
				}
			}

			updateToFile(jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void changeDisplayName(int id, @NotNull String newDisplayName) {
		changeAccountInfo(id, "dispname", newDisplayName);
	}

	public void changeStatus(int id, @NotNull String status) {
		changeAccountInfo(id, "status", status);
	}

	public void changePasswordHash(int id, @NotNull String passhash) {
		changeAccountInfo(id, "passhash", passhash);
	}
}
