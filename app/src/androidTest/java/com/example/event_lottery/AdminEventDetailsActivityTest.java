import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.event_lottery.AdminEventDetailsActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AdminEventDetailsActivityTest {

    private FirebaseFirestore mockDb;

    @Rule
    public ActivityTestRule<AdminEventDetailsActivity> activityRule =
            new ActivityTestRule<>(AdminEventDetailsActivity.class);

    @Before
    public void setup() {
        mockDb = Mockito.mock(FirebaseFirestore.class);
    }

    @Test
    public void testEventDeletion() {
        // Mock Firestore deletion
        ActivityScenario<AdminEventDetailsActivity> scenario = activityRule.getScenario();

        scenario.onActivity(activity -> {
            FirebaseFirestore db = activity.getDb(); // Assuming getDb() is a method that returns the FirebaseFirestore instance

            // Mock a successful deletion
            Mockito.doAnswer(invocation -> {
                ((FirebaseFirestore.OnCompleteListener<QuerySnapshot>) invocation.getArguments()[0]).onComplete(task -> {
                    assertTrue(task.isSuccessful());
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        assertTrue(doc.exists());
                    }
                });
                return null;
            }).when(db).collection("events").document(activity.getEventId()).delete();

            // Click the remove button and check for deletion
            onView(withId(R.id.remove_event_button)).perform(click());
        });
    }
}