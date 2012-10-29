package TFC.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import TFC.*;
import TFC.Core.ColorizerGrassTFC;
import TFC.Core.TFC_Climate;
import TFC.Core.TFC_Core;
import TFC.Core.TFC_Settings;
import TFC.TileEntities.TileEntityPartial;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockGrass extends net.minecraft.src.BlockGrass
{
    public BlockGrass(int par1, int par2)
    {
        super(par1);
        this.setTickRandomly(true);
        this.blockIndexInTexture = par2;
    }

    @Override
    public String getTextureFile()
    {
        return "/bioxx/terrablocks2.png";
    }

    @Override
    public int damageDropped(int i) {
        return i;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    @Override
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par1 == 1 ? 255 : (par1 == 0 ? (this.blockIndexInTexture+16) : (this.blockIndexInTexture + par2));
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    @Override
    public int getBlockTexture(IBlockAccess access, int xCoord, int yCoord, int zCoord, int par5)
    {
    	Block blk = Block.blocksList[TFC_Core.getTypeForDirt(access.getBlockMetadata(xCoord, yCoord, zCoord))];
    	
        if (par5 == 1)//top
        {
            return 255;
        }
        else if (par5 == 0)//bottom
        {
            return this.blockIndexInTexture + access.getBlockMetadata(xCoord, yCoord, zCoord);
        }
        else if (par5 == 2)//-Z
        {
            if(TFC_Settings.enableBetterGrass == true && access.getBlockMaterial(xCoord, yCoord-1, zCoord-1) == Material.grass)
                return 255;
            else
                return this.blockIndexInTexture + access.getBlockMetadata(xCoord, yCoord, zCoord);
        }
        else if (par5 == 3)//+Z
        {
            if(TFC_Settings.enableBetterGrass == true && access.getBlockMaterial(xCoord, yCoord-1, zCoord+1) == Material.grass)
                return 255;
            else
                return this.blockIndexInTexture + access.getBlockMetadata(xCoord, yCoord, zCoord);
        }
        else if (par5 == 4)//-X
        {
            if(TFC_Settings.enableBetterGrass == true && access.getBlockMaterial(xCoord-1, yCoord-1, zCoord) == Material.grass)
                return 255;
            else
                return this.blockIndexInTexture + access.getBlockMetadata(xCoord, yCoord, zCoord);
        }
        else if (par5 == 5)//+X
        {
            if(TFC_Settings.enableBetterGrass == true && access.getBlockMaterial(xCoord+1, yCoord-1, zCoord) == Material.grass)
                return 255;
            else
                return this.blockIndexInTexture + access.getBlockMetadata(xCoord, yCoord, zCoord);
        }
        return this.blockIndexInTexture;
    }


    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return TerraFirmaCraft.proxy.grassColorMultiplier(par1IBlockAccess, par2, par3, par4);
    }

    @Override
    public int getBlockColor()
    {
        double var1 = 0.5D;
        double var3 = 1.0D;
        return ColorizerGrassTFC.getGrassColor(var1, var3);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @Override
    public int getRenderColor(int par1)
    {
        return this.getBlockColor();
    }
    @Override
    public int getRenderType()
    {
        return TFCBlocks.grassRenderId;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int par2, int par3, int par4, Random rand)
    {
        if (!world.isRemote)
        {
            if (world.getBlockLightValue(par2, par3 + 1, par4) < 4 && Block.lightOpacity[world.getBlockId(par2, par3 + 1, par4)] > 2)
            {
                world.setBlockAndMetadataWithNotify(par2, par3, par4, TFC_Core.getTypeForDirt(world.getBlockMetadata(par2, par3, par4)), world.getBlockMetadata(par2, par3, par4));
            }
            else if (world.getBlockLightValue(par2, par3 + 1, par4) >= 9)
            {            	
                for (int var6 = 0; var6 < 4; ++var6)
                {
                    int x = par2 + rand.nextInt(3) - 1;
                    int y = par3 + rand.nextInt(5) - 3;
                    int z = par4 + rand.nextInt(3) - 1;

                    float rain = TFC_Climate.getRainfall(x, y + 1, z);
                    
                    int id = world.getBlockId(x, y, z);
                    int meta = world.getBlockMetadata(x, y, z);

                    if (TFC_Core.isDirt(id) && rand.nextInt(10) == 0 &&
                    		world.getBlockLightValue(x, y + 1, z) >= 4 && world.getBlockMaterial(x, y + 1, z) != Material.water)
                    {
                        world.setBlockAndMetadataWithNotify(x, y, z, TFC_Core.getTypeForGrassWithRain(meta, rain), meta);
                    }
                    else if (TFC_Core.isClay(id) && world.getBlockLightValue(x, y + 1, z) >= 4 && rand.nextInt(10) == 0 && world.getBlockMaterial(x, y + 1, z) != Material.water)
                    {
                        world.setBlockAndMetadataWithNotify(x, y, z, TFC_Core.getTypeForClayGrass(meta), meta);
                    }
                    else if (TFC_Core.isPeat(id) && world.getBlockLightValue(x, y + 1, z) >= 4 && rand.nextInt(10) == 0 && world.getBlockMaterial(x, y + 1, z) != Material.water)
                    {
                        world.setBlockWithNotify(x, y, z, TFCBlocks.PeatGrass.blockID);
                    }
                    else if (TFC_Core.isGrass(id) && !TFC_Core.isDryGrass(id) && world.getBlockLightValue(x, y + 1, z) >= 4 && rand.nextInt((int) ((16400-rain)/4)) == 0 && 
                    		world.getBlockMaterial(x, y + 1, z) != Material.water && world.getBlockId(x, y+1, z) == 0)
                    {
                    	world.setBlockAndMetadataWithNotify(x, y+1, z, Block.tallGrass.blockID, 1);
                    }
                }
            }
            world.markBlockNeedsUpdate(par2, par3, par4);
        }
    }

    @Override
    public void onEntityWalking(World world, int i, int j, int k, Entity par5Entity) 
    {
        if (!world.isRemote)
        {
            Random R = new Random();
            if(!BlockCollapsable.isNearSupport(world, i, j, k) && BlockDirt.canFallBelow(world, i, j - 1, k) && R.nextInt(10) == 0)
            {
                int meta = world.getBlockMetadata(i, j, k);
                world.setBlockAndMetadataWithNotify(i, j, k, TFC_Core.getTypeForDirt(meta), meta);
            }
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.blocksList[TFC_Core.getTypeForDirt(par1)].idDropped(par1, par2Random, par3);
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if(!world.blockExists(i, j-1, k))
        {
            int meta = world.getBlockMetadata(i, j, k);
            	world.setBlockAndMetadataWithNotify(i, j, k, TFC_Core.getTypeForDirt(meta), meta);
        }
    }
}
