package inf112.Fefe.model.tiles;

import com.badlogic.gdx.physics.box2d.World;

import inf112.Fefe.model.tiles.powerups.Collectible;
import inf112.Fefe.model.tiles.powerups.DoubleDash;

/**
 * The different types of tiles in game
 */
public enum TileType {
    GROUND {
        @Override
        public ITile instanciate(World world, int row, int col) {
            return new GroundTile(world, row, col);
        }
    },
    SPIKE_UP {
        @Override
        public ITile instanciate(World world, int row, int col) {
            return new SpikeUpTile(world, row, col);
        }
    },
    SPIKE_DOWN {
        @Override
        public ITile instanciate(World world, int row, int col) {
            return new SpikeDownTile(world, row, col);
        }
    },
    SPIKE_RIGHT {
        @Override
        public ITile instanciate(World world, int row, int col) {
            return new SpikeRightTile(world, row, col);
        }
    },
    SPIKE_LEFT {
        @Override
        public ITile instanciate(World world, int row, int col) {
            return new SpikeLeftTile(world, row, col);
        }
    },
    LOADING_ZONE {
        @Override
        public ITile instanciate(World world, int row, int col) {
            return new LoadingZoneTile(world, row, col);
        }
    },
    DOUBLE_DASH {
        @Override
        public ITile instanciate(World world, int row, int col) {
            return new DoubleDash(world, row, col);
        }
    },
    COLLECTIBLE {
        @Override
        public ITile instanciate(World world, int row, int col) {
            return new Collectible(world, row, col);
        }
    },
    INFO {
        @Override
        public ITile instanciate(World world, int row, int col) {
            return new InfoTile(world, row, col);
        }
    };

    public abstract ITile instanciate(World world, int row, int col);
}
