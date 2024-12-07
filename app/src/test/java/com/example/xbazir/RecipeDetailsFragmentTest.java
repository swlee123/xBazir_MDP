import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.xbazir.ui.RecipeRecommender.RecipeDetailsFragment;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RecipeDetailsFragmentTest {

    private RecipeDetailsFragment fragment;

    @Before
    public void setUp() {
        fragment = new RecipeDetailsFragment();
    }

    @Test
    public void testParseJsonArray() throws Exception {
        // Mock JSONArray
        JSONArray mockJsonArray = mock(JSONArray.class);
        when(mockJsonArray.length()).thenReturn(2);
        when(mockJsonArray.getString(0)).thenReturn("Ingredient1");
        when(mockJsonArray.getString(1)).thenReturn("Ingredient2");

        List<String> result = fragment.parseJsonArray(mockJsonArray.toString());

        // Validate the parsed data
        assertEquals(0, result.size());
        assertEquals("Ingredient1", result.get(0));
        assertEquals("Ingredient2", result.get(1));
    }

    @Test
    public void testParseJsonArrayWithInvalidData() {
        String invalidJsonArrayString = "[Invalid JSON";
        List<String> result = fragment.parseJsonArray(invalidJsonArrayString);

        // Validate that parsing invalid JSON returns an empty list
        assertEquals(0, result.size());
    }
}
