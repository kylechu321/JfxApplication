package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InitDataTest {

    private InitData initData;

    @BeforeEach
    void setup() {
        initData = new InitData();
    }

    @Test
    void groupedProjectRepresentativesTest() {
        AvailableProject p1 = new AvailableProject(1, "Alpha", "Location1", "mon", 100, 5, 10, true);
        AvailableProject p2 = new AvailableProject(2, "Alpha", "Location2", "tue", 100, 5, 10, true);
        AvailableProject p3 = new AvailableProject(3, "Beta", "Location3", "wed", 100, 5, 10, true);

        initData.setAvailableProjects(new ArrayList<>(List.of(p1, p2, p3)));
        initData.groupProjectsByName();

        List<AvailableProject> reps = initData.getGroupedProjectRepresentatives();
        assertEquals(2, reps.size()); // "Alpha" and "Beta"
        assertTrue(reps.contains(p1) || reps.contains(p2));
        assertTrue(reps.contains(p3));

    }

    @Test
    void enabledProjectsTest() {
        AvailableProject enabled = new AvailableProject(1, "Alpha", "Loc", "mon", 100, 5, 10, true);
        AvailableProject disabled = new AvailableProject(2, "Beta", "Loc", "tue", 100, 5, 10, false);

        initData.setAvailableProjects(new ArrayList<>(List.of(enabled, disabled)));

        List<AvailableProject> result = initData.getEnabledProjects();
        assertEquals(1, result.size());
        assertTrue(result.contains(enabled));
        assertFalse(result.contains(disabled));
    }

}