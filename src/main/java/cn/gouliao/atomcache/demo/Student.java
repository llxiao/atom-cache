package cn.gouliao.atomcache.demo;

import cn.gouliao.atomcache.needimpl.IGetCacheKey;
import cn.gouliao.atomcache.util.CacheNameUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 类说明
 *
 * @author shawn
 * @since 2017/12/10
 */
@Data
public class Student implements Serializable, IGetCacheKey {
    @SerializedName("studentID")
    @Expose
    private String studentID;
    @SerializedName("studentName")
    @Expose
    private String studentName;
    @SerializedName("studentFriend")
    @Expose
    private Set<Friend> studentFriend;


    @Override
    @JsonIgnore
    public String getCacheKey() {
        //这里的类为你具体的缓存类 并不是当前类 更新和删除的规则相同
        String name = Student.class.getName();
        return CacheNameUtil.getCacheName("{}_{}", name, getStudentID());
    }


    public static final class Builder {
        private String studentID;
        private String studentName;
        private Set<Friend> studentFriend;

        public Builder() {
        }

        public static Builder aStudent() {
            return new Builder();
        }

        public Builder withStudentID(String studentID) {
            this.studentID = studentID;
            return this;
        }

        public Builder withStudentName(String studentName) {
            this.studentName = studentName;
            return this;
        }

        public Builder withStudentFriend(Set<Friend> studentFriend) {
            this.studentFriend = studentFriend;
            return this;
        }

        public Student build() {
            Student student = new Student();
            student.setStudentID(studentID);
            student.setStudentName(studentName);
            student.setStudentFriend(studentFriend);
            return student;
        }
    }
}
