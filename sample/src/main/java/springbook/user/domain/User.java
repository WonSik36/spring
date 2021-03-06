/*
*   ref: toby's spring 3.1
*    author: toby.epril.com
*/

package springbook.user.domain;

public class User{
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;
    private String email;

    public User() {}
    
    public User(String id, String name, String pw, Level level, int login, int recommend, String email) {
    	this.id = id;
    	this.name = name;
    	this.password = pw;
    	this.level = level;
    	this.login = login;
    	this.recommend = recommend;
    	this.email = email;
    }
    
    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
    
    public Level getLevel(){
        return level;
    }

    public void setLevel(Level level){
        this.level = level;
    }
    
    public int getLogin(){
        return login;
    }

    public void setLogin(int login){
        this.login = login;
    }
    
    public int getRecommend(){
        return recommend;
    }
    
    public void setRecommend(int recommend){
        this.recommend = recommend;
    }
    
    public void upgradeLevel() {
    	Level nextLevel = this.level.nextLevel();
    	if(nextLevel == null) {
    		throw new IllegalStateException(this.level+" can't upgrade");
    	}else {
    		this.level = nextLevel;
    	}
    }
    
    public String getEMail(){
        return email;
    }

    public void setEMail(String email){
        this.email = email;
    }
}