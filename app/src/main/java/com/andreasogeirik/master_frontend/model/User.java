package com.andreasogeirik.master_frontend.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andreas on 26.01.2016.
 */
public class User implements Serializable {
    private int id;
    private String email;
    private boolean enabled;
    private String password;
    private String firstname;
    private String lastname;
    private String location;
    private String imageUri;
    private String thumbUri;
    private boolean admin = false;
    private Set<Friendship> friends = new HashSet<>();
    private Set<Friendship> requests = new HashSet<>();
    private Set<Post> posts = new HashSet<>();

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(String email, String password, String firstname, String lastname, String location) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.location = location;
    }

    public User(int id, String email, boolean enabled, String firstname, String lastname, String location,
                String imageUri, String thumbUri, boolean admin) {
        this.id = id;
        this.email = email;
        this.enabled = enabled;
        this.firstname = firstname;
        this.lastname = lastname;
        this.location = location;
        this.imageUri = imageUri;
        this.thumbUri = thumbUri;
        this.admin = admin;
    }

    public void copy(User user) {
        this.id = user.id;
        this.email = user.email;
        this.enabled = user.enabled;
        this.password = user.password;
        this.firstname = user.firstname;
        this.lastname = user.lastname;
        this.location = user.location;
        this.imageUri = user.imageUri;
        this.thumbUri = user.thumbUri;
    }

    //TODO: hvis vi skal laste en bruker med friends eller posts fra json, gjør det i konstruktøren her(se Post/Post)
    public User(JSONObject json) throws JSONException {
        id = json.getInt("id");
        email = json.getString("email");
        enabled = json.getBoolean("enabled");
        firstname = json.getString("firstname");
        lastname = json.getString("lastname");
        location = json.getString("location");
        imageUri = json.getString("imageUri");
        thumbUri = json.getString("thumbUri");

        //json sends null as a string
        if(imageUri.equals("null")) {
            imageUri = "";
        }
        if(!json.isNull("admin")) {
            admin = json.getBoolean("admin");
        }
    }

    // Used to edit users
    public JSONObject createJSONWithId() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("email", this.email);
        jsonObject.put("firstname", this.firstname);
        jsonObject.put("lastname", this.lastname);
        jsonObject.put("location", this.location);
        jsonObject.put("imageUri", this.imageUri);
        jsonObject.put("thumbUri", this.thumbUri);

        return jsonObject;
    }

    // Used to create users
    public JSONObject createJSONWithoutId() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", this.email);
        jsonObject.put("password", this.password);
        jsonObject.put("firstname", this.firstname);
        jsonObject.put("lastname", this.lastname);
        jsonObject.put("location", this.location);
        jsonObject.put("imageUri", this.imageUri);
        jsonObject.put("thumbUri", this.thumbUri);


        return jsonObject;
    }

    public Friendship findFriend(int friendshipId) {
        Iterator<Friendship> it = friends.iterator();
        while(it.hasNext()) {
            Friendship friendship = it.next();
            if(friendship.getId() == friendshipId) {
                return friendship;
            }
        }
        return null;
    }

    public Friendship findRequest(int friendshipId) {
        Iterator<Friendship> it = requests.iterator();
        while(it.hasNext()) {
            Friendship friendship = it.next();
            if(friendship.getId() == friendshipId) {
                return friendship;
            }
        }
        return null;
    }

    public boolean isFriendWith(User user) {
        Iterator<Friendship> it = friends.iterator();
        while(it.hasNext()) {
            if(it.next().getFriend().equals(user)) {
                return true;
            }
        }
        return false;
    }

    /*
     * I requested the other user
     */
    public boolean iHaveRequested(User user) {
        Iterator<Friendship> it = requests.iterator();
        while(it.hasNext()) {
            Friendship request = it.next();
            if(request.getFriend().equals(user)) {
                return request.isMyRequest();
            }
        }
        return false;
    }

    /*
     * The other user requested me
     */
    public boolean iWasRequested(User user) {
        Iterator<Friendship> it = requests.iterator();
        while(it.hasNext()) {
            Friendship request = it.next();
            if(request.getFriend().equals(user)) {
                return !request.isMyRequest();
            }
        }
        return false;
    }

    public void addRequest(Friendship friendship) {
        requests.add(friendship);
    }

    public void removeFriendship(int friendshipId) {
        Friendship friendship = new Friendship();
        friendship.setId(friendshipId);
        friends.remove(friendship);
        requests.remove(friendship);
    }

    public void goFromRequestToFriend(int friendshipId) {
        Iterator<Friendship> it = requests.iterator();
        while(it.hasNext()) {
            Friendship friendship = it.next();
            if(friendship.getId() == friendshipId) {
                requests.remove(friendship);
                friendship.setStatus(Friendship.FRIENDS);
                friends.add(friendship);
                return;
            }
        }
    }

    public void addPosts(Collection<Post> posts) {
        if(posts != null) {
            this.posts.addAll(posts);
        }
    }

    public void addFriends(Collection<Friendship> friendships) {
        Iterator<Friendship> it = friendships.iterator();

        while(it.hasNext()) {
            Friendship friendship = it.next();
            if(friendship.getStatus() == Friendship.FRIENDS) {
                this.friends.add(friendship);
            }
            else {
                this.requests.add(friendship);
            }
        }
    }

    public void copyUser(User user) {
        this.id = user.id;
        this.firstname = user.firstname;
        this.lastname = user.lastname;
        this.imageUri = user.imageUri;
        this.email = user.email;
        this.location = user.location;
        this.enabled = user.enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Set<Friendship> getFriends() {
        return friends;
    }

    public void setFriends(Set<Friendship> friends) {
        this.friends = friends;
    }

    public Set<Friendship> getRequests() {
        return requests;
    }

    public void setRequests(Set<Friendship> requests) {
        this.requests = requests;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        Iterator it = null;
        if(friends != null) {
            it = friends.iterator();
        }
        String user = "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", location='" + location + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", friends=";
        if(it != null) {
            while (it.hasNext()) {
                user += it.next();
            }
        }
        user += '}';
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}