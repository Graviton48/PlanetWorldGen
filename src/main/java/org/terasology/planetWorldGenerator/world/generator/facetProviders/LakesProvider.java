package org.terasology.planetWorldGenerator.world.generator.facetProviders;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2f;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

@RegisterPlugin
@Updates(@Facet(SurfaceHeightFacet.class))
public class LakesProvider implements FacetProviderPlugin {

    private Noise lakeNoise;

    @Override
    public void setSeed(long seed) {
        lakeNoise = new SubSampledNoise(new SimplexNoise(seed), new Vector2f(0.01f, 0.01f), 1);
    }

    @Override
    public void process (GeneratingRegion region){
        float lakeDepth = 35;
        SurfaceHeightFacet facet = region.getRegionFacet(SurfaceHeightFacet.class);

        Rect2i processRegion = facet.getWorldRegion();
        for (BaseVector2i position : processRegion.contents()) {
            float addDepth = lakeNoise.noise(position.x(), position.y() * lakeDepth);
            addDepth = TeraMath.clamp(addDepth, -lakeDepth, 0);
            facet.setWorld(position, facet.getWorld(position) + addDepth);
        }
    }
}