package nl.shanelab.kwetter.util;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class WeldJUnit4Runner extends BlockJUnit4ClassRunner {

    public WeldJUnit4Runner(final Class<Object> clazz) throws InitializationError {
        super(clazz);
    }
    @Override
    protected Object createTest() {
        return WeldContext.SINGLETON.getBean(getTestClass().getJavaClass());
    }

    @NoArgsConstructor
    @RequiredArgsConstructor
    private static class WeldContext {
        @NonNull
        private WeldContainer weldContainer;

        static final WeldContext SINGLETON = new WeldContext(new Weld().initialize());

        @SuppressWarnings("deprecation")
        public <T> T getBean(final Class<T> type) {
            return this.weldContainer.instance().select(type).get();
        }
    }
}