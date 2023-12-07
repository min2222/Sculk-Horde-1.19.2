package com.github.sculkhorde.common.entity.components;


import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ImprovedFlyingNavigator extends FlyingPathNavigation {
    private float distancemodifier = 0.75F;
    public ImprovedFlyingNavigator(Mob entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
    }
    public ImprovedFlyingNavigator(Mob entitylivingIn, Level worldIn, float distancemodifier) {
        super(entitylivingIn, worldIn);
        this.distancemodifier = distancemodifier;
    }
    protected void followThePath() {
        Vec3 mobPosition = this.getTempMobPos();
        this.maxDistanceToWaypoint = this.mob.getBbWidth() * distancemodifier;
        Vec3i nextNodePosition = this.path.getNextNodePos();
        double distanceX = Math.abs(this.mob.getX() - ((double) nextNodePosition.getX() + 0.5D));
        double distanceY = Math.abs(this.mob.getY() - (double) nextNodePosition.getY());
        double distanceZ = Math.abs(this.mob.getZ() - ((double) nextNodePosition.getZ() + 0.5D));
        boolean isNearNextNode  = distanceX < (double) this.maxDistanceToWaypoint && distanceZ < (double) this.maxDistanceToWaypoint && distanceY < 1.0D;
        boolean canCutCorner = this.canCutCorner(this.path.getNextNode().type);
        boolean shouldTargetNextNodeInDirection = this.shouldTargetNextNodeInDirection(mobPosition);
        boolean shouldAdvance = isNearNextNode || canCutCorner && shouldTargetNextNodeInDirection;
        if (shouldAdvance) {
            this.path.advance();
        }
        this.doStuckDetection(mobPosition);
    }
    private boolean shouldTargetNextNodeInDirection(Vec3 currentPosition) {
        if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
            return false;
        } else {
            Vec3 vector3d = Vec3.atBottomCenterOf(this.path.getNextNodePos());
            if (!currentPosition.closerThan(vector3d, 2.0D)) {
                return false;
            } else {
                Vec3 vector3d1 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
                Vec3 vector3d2 = vector3d1.subtract(vector3d);
                Vec3 vector3d3 = currentPosition.subtract(vector3d);
                return vector3d2.dot(vector3d3) > 0.0D;
            }
        }
    }

}
