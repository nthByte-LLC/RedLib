package redempt.redlib.region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A Region made of a set of Blocks. Faster but more memory-intensive than MultiRegion. Limited functionality.
 */
public class BlockSetRegion extends Region {
	
	private static Block[] toBlocks(Location... locations) {
		Block[] blocks = new Block[locations.length];
		for (int i = 0; i < locations.length; i++) {
			blocks[i] = locations[i].getBlock();
		}
		return blocks;
	}
	
	private Set<Block> set = new HashSet<>();
	private World world = null;
	
	/**
	 * Constructs a BlockSetRegion from a set of Blocks
	 * @param set The set of Blocks
	 */
	public BlockSetRegion(Set<Block> set) {
		set.forEach(this::add);
	}
	
	/**
	 * Constructs a BlockSetRegion from a vararg of Blocks
	 * @param blocks
	 */
	public BlockSetRegion(Block... blocks) {
		Arrays.stream(blocks).forEach(this::add);
	}
	
	/**
	 * Checks whether this BlockSetRegion contains a given Location
	 * @param loc The location to check
	 * @return Whether this BlockSetRegion contains the given Location
	 */
	@Override
	public boolean contains(Location loc) {
		return set.contains(loc.getBlock());
	}
	
	/**
	 * Checks whether this BlockSetRegion contains a given Block
	 * @param block The block to check
	 * @return Whether this BlockSetRegion contains the given Block
	 */
	public boolean contains(Block block) {
		return set.contains(block);
	}
	
	/**
	 * @return The block volume of this BlockSetRegion
	 */
	@Override
	public int getBlockVolume() {
		return set.size();
	}
	
	/**
	 * Also returns the block volume of this BlockSetRegion - cannot include partial blocks
	 * @return
	 */
	@Override
	public double getVolume() {
		return set.size();
	}
	
	/**
	 * @return Whether this Region is a non-cuboid variant
	 */
	@Override
	public boolean isMulti() {
		return true;
	}
	
	/**
	 * Moves this BlockSetRegion a given amount
	 * @param v The vector to be applied to all blocks in this BlockSetRegion
	 */
	@Override
	public void move(Vector v) {
		Set<Block> clone = new HashSet<>();
		for (Block block : set) {
			clone.add(block.getLocation().add(v).getBlock());
		}
		set = clone;
	}
	
	/**
	 * Adds a location's block to this BlockSetRegion
	 * @param loc The location to add
	 */
	public void add(Location loc) {
		add(loc.getBlock());
	}
	
	/**
	 * Adds a block to this BlockSetRegion
	 * @param block The block to add
	 */
	public void add(Block block) {
		if (world == null) {
			world = block.getWorld();
			start = block.getLocation();
			end = block.getLocation().add(1, 1, 1);
		} else if (!block.getWorld().equals(world)) {
			throw new IllegalArgumentException("All blocks must be in the same world!");
		}
		set.add(block);
		end = block.getLocation().add(1, 1, 1);
	}
	
	/**
	 * Removes a Block from this BlockSetRegion
	 * @param block The Block to remove
	 */
	public void remove(Block block) {
		set.remove(block);
	}
	
	/**
	 * Removes a location's block from this BlockSetRegion
	 * @param loc The location to remove
	 */
	public void remove(Location loc) {
		set.remove(loc.getBlock());
	}
	
	/**
	 * Checks whether this BlockSetRegion overlaps another Region
	 * @param other The other Region
	 * @return Whether this BlockSetRegion overlaps the other Region
	 */
	@Override
	public boolean overlaps(Region other) {
		return set.stream().map(b -> b.getLocation()).anyMatch(other::contains);
	}
	
	/**
	 * @return A copy of this BlockSetRegion
	 */
	@Override
	public BlockSetRegion clone() {
		return new BlockSetRegion(set);
	}
	
	/**
	 * @return The World this BlockSetRegion is in
	 */
	@Override
	public World getWorld() {
		return world;
	}
	
	/**
	 * Streams all the Blocks in this BlockSetRegion
	 * @return A Stream of all the Blocks in this BlockSetRegion
	 */
	@Override
	public Stream<Block> stream() {
		return set.stream();
	}
	
	@Override
	public void forEachBlock(Consumer<Block> lambda) {
		stream().forEach(lambda);
	}
	
}
