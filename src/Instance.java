
public class Instance {
	   public enum PersonFunnel implements Funnel<Person> {
		     INSTANCE;
		     public void funnel(Person person, PrimitiveSink into) {
		       into.putUnencodedChars(person.getFirstName())
		           .putUnencodedChars(person.getLastName())
		           .putInt(person.getAge());
		     }
		   }
}
