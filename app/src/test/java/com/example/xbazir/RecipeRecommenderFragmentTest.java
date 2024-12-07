import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.xbazir.ui.RecipeRecommender.RecipeRecommenderFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecommenderFragmentTest {

    @Mock
    Context mockContext;

    private RecipeRecommenderFragment fragment;
    private ConstraintLayout mockLayout;
    private Flow mockFlow;

    private List<String> list;
    private List<Integer> chipIds;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        fragment = new RecipeRecommenderFragment();
        list = new ArrayList<>();
        chipIds = new ArrayList<>();
        mockLayout = new ConstraintLayout(mockContext);
        mockFlow = new Flow(mockContext);
    }

    @Test
    public void testAddSelectableChipToFlow() {
        fragment.addSelectableChipToFlow("Test Chip", mockFlow, list, chipIds);

        // Verify the chip is added to the list
        assertEquals(1, list.size());
        assertEquals("Test Chip", list.get(0));
    }

    @Test
    public void testAddDuplicateChipToFlow() {
        list.add("Test Chip");
        fragment.addSelectableChipToFlow("Test Chip", mockFlow, list, chipIds);

        // Verify that duplicates are not added
        assertEquals(1, list.size());
    }
}
