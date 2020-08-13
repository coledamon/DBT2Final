package models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {

        
        @JsonProperty("_id") private String id;
        @JsonProperty("_rev") private String revision;
        private String name;
        private String birthday;
        private List<Order> orders;
        
        public String getId() {
            return id;
        }
        public void setId(String s) {
            id = s;
        }

        public String getRevision() {
        	return revision;
        }
        public void setRevision(String s) {
            revision = s;
        }

        public void setName(String s) {
            name = s;
        }
        public String getName() {
            return name;
        }
        
		public String getBirthday() {
			return birthday;
		}
		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}
		
		public List<Order> getOrders() {
			return orders;
		}
		public void setOrders(List<Order> orders) {
			this.orders = orders;
		}
		
		@Override
		public String toString() {
			return ("id: " + id + "\nname: " + name + "\nbirthday: " + birthday + "\norders: \n" + orders + "\n");
		}
        
         
}
