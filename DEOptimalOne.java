
import java.util.Random;
/////////
// author:xiaobingscuer
// topic:DE algorithm one,关于差分进化算法（一）
// 这是用 差分进化算法 求解一维 最优问题: min f(x),x的范围(-5,5)
// 此程序中，f(x)=(x-2)^2,最优解为 2.000
// 差分进化算法采用实数编码，轮盘赌选择，繁殖方式为newPopulation[i]=population[r1]+CROSS_FACTOR*(population[r2]-population[r3])
// 由于f(x)>0,故个体适应度评估函数为 1/f(x)
// 最后输出比遗传算法的结果更快、更稳定、更优
/////////
public class DEOptimalOne {
	// 种群规模
	private final static  int POPULATION_SCALE=1000;
	// 进化代数
	private final int EVALUTION_NUM=100;
	// 交叉概率
	private final float CROSS_PROBABLITY=0.1F;
	// 交叉因子
	private final float CROSS_FACTOR=0.5F;
	// x域
	float max=5;
	float min=-5;
		
	public DEOptimalOne() {
		super();
	}
	
	// 生成种群  // 实数编码
	public float[] generatePoupulation(int scale){
		float[] population = new float[scale];
		Random rand=new Random();
		for(int i=0;i<scale;i++){
			population[i]=rand.nextFloat()*10-5;		// 随机生成(-5,5)区间的个体
		}
		return population;
	}
	// 个体适应度评估 
	public double evaluatePerson(float person){
		double fitness=1/Math.pow(person-2,2); 			// 1/f(x),即 1/(x-2)^2
		return fitness;
	}
	// 种群适应度表
	public double[] fitnessList(float[] population){
		double[] fitList=new double[population.length]; // 每个个体的适应度表fitness
		double sum=0;
		for(int i=0;i<population.length;i++){
			fitList[i]=evaluatePerson(population[i]);	// 评估种群population中的个体population[i]的适应度
			sum+=fitList[i];							// 对这些适应度进行累加求和
		}
		for(int i=0;i<population.length;i++){
			fitList[i]=fitList[i]/sum;				    // 求每个适应度所占比例，population[i]/sum，即将适应度转化为概率
		}
		return fitList;
	}
	// 选择 //和之前的适应度比较，适应度比以前大就保留新的个体，否则保留以前的个体
	public float[] choice(float[] population,double[] fitnessList,float[] newPopulation,double[] newFitnessList){
		for(int i=0;i<population.length;i++){
			if(newFitnessList[i]<fitnessList[i]){
				newPopulation[i]=population[i];
			}
		}
		return newPopulation;
	}
	// 繁殖,将遗传算法的交叉操作和变异操作合并为繁殖操作
	public float[] breed(float[] population){
		float[] newPopulation =new float[population.length];
		Random rand=new Random();
		int r1,r2,r3;
		for(int i=0;i<population.length;i++){
			if(rand.nextFloat()<CROSS_PROBABLITY){		// 判断是否需要繁殖
				r1=rand.nextInt(population.length);
				do{
					r2=rand.nextInt(population.length);
				}while(r2==r1);
				do{
					r3=rand.nextInt(population.length);
				}while(r3==r2||r3==r1);
				newPopulation[i]=population[r1]+CROSS_FACTOR*(population[r2]-population[r3]); // 繁殖方式
				if(newPopulation[i]>5){					// 判断个体是否在x域里
					newPopulation[i]=5;
				}if(newPopulation[i]<-5){
					newPopulation[i]=-5;
				}
			}else{
				newPopulation[i]=population[i];
			}
		}
		return newPopulation;
	}
	// 进化
	public float[] evlution(float[] population){
		float[] newPopulation = null;
		double[] fitList;
		double[] newFitList;
		for(int i=0;i<EVALUTION_NUM;i++){
			fitList=fitnessList(population);			// 上一代适应度评估
			newPopulation=breed(population);			// 繁殖
			newFitList=fitnessList(newPopulation);		// 新一代适应度评估
			newPopulation=choice(population,fitList,newPopulation,newFitList);// 个体选择
			population=newPopulation;
			
		}
		return newPopulation;
	}
	// 从进化相应代数的种群中选出最优个体
	public float[] bestPerson(float[] population){
		float[] bestPer = new float[2];
		double[] fitList=fitnessList(population);
		for(int i=population.length-1;i>0;i--){
			fitList[i]-=fitList[i-1];					// 将累计概率的适应度表转回为概率
		}		
		for(int i=0;i<population.length;i++){
			if(bestPer[0]<fitList[i]){					// 选出适应度值最大的个体
				bestPer[0]=(float) fitList[i];			// bestPer[0]记录最大是适应度值
				bestPer[1]=population[i];				// bestPer[1]记录最优解
			}
		}
		return bestPer;
	}
	
	public static void main(String[] args) {
		
		long startTime=System.currentTimeMillis();			// 开始时间
		DEOptimalOne deOptimal=new DEOptimalOne();			// 定义对象
		float[] bestPer=deOptimal.bestPerson(deOptimal.evlution(deOptimal.generatePoupulation(POPULATION_SCALE))); // 方法调用
		long endTime=System.currentTimeMillis();			// 结束时间	
		System.out.print("最优个体的适应度："+bestPer[0]+"\n"+"最优解："+bestPer[1]+"\n"+"用时(ms):"+(endTime-startTime));
		
	}

}
