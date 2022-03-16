package com.testpart1;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.testpart1.entity.PPart;
import com.testpart1.entity.PPartChainDetails;
import com.testpart1.mapper.PPartChainDetailsMapper;
import com.testpart1.mapper.PPartChainMapper;
import com.testpart1.mapper.PPartMapper;
import com.testpart1.service.PPartChainDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class TestPart1ApplicationTests {
    @Autowired
    private PPartMapper pPartMapper;
    @Autowired
    private PPartChainMapper pPartChainMapper;
    @Autowired
    private PPartChainDetailsMapper pPartChainDetailsMapper;
    @Autowired
    private PPartChainDetailsService pPartChainDetailsService;


    private List<PPartChainDetails> pPartChainDetails;//阻止并发,否则会有线程安全问题

    private Map<Long,PPartChainDetails> PPartChainDetailsMap=new LinkedHashMap<>();
//    //0.用户寄存深度层级和排序的集合
//    private Map<Integer,Integer> depositMap=new LinkedHashMap<>();
//    //跟节点的排序
//    private Integer rootOrder=0;

    private Map<Long,Integer> rootOrderMap=new LinkedHashMap<>();

    @Test
    void contextLoads() {
        //数据量大,程序会运行很久..
//        System.out.println(pPartChainDetailsService);
        //1.将PPartChainDetails记录全部查出来
        pPartChainDetails = pPartChainDetailsMapper.selectList(new QueryWrapper<PPartChainDetails>().orderByAsc("id"));

//        System.out.println(pPartChainDetails);//18000+

        //.考虑到后面要进行递归查询,所以这里先遍历一次来赋上id做成key-value
        for (PPartChainDetails pPartChainDetail : pPartChainDetails) {
            PPartChainDetailsMap.put(pPartChainDetail.getPartId(),pPartChainDetail);
            //如果是跟节点,则直接记录其下的order值存入map
            if(pPartChainDetail.getPartId().equals(pPartChainDetail.getParentPartId())){//根节点
                rootOrderMap.put(pPartChainDetail.getPartId(),1);//从1开始..因为根节点是0
            }

        }

        //2.遍历每个pcd记录
        for (PPartChainDetails pPartChainDetail : pPartChainDetails) {
            pPartChainDetail=handle(pPartChainDetail);//处理完的pPartChainDetail
        }

        //3.进行批量更新操作
        boolean b = pPartChainDetailsService.saveOrUpdateBatch(pPartChainDetails, 500);
        if(b){
            System.out.println("success");
        }else {
            System.out.println("faild");
        }
    }


    private PPartChainDetails handle(PPartChainDetails pPartChainDetail){
        //3.判断记录的ppid在p表是否存在:不存在的话,in=-1,de=-1
        Long parentId=pPartChainDetail.getParentPartId();
        PPart pPart = pPartMapper.selectById(parentId);
        if(pPart==null){//父节点不存在
            pPartChainDetail.setChainDepth(-1);
            pPartChainDetail.setChainIndex(-1);
//                pPartChainDetail.setPartId(-1l);//父节点id设置为null
            pPartChainDetail.setFlag(true);//表示已经检查过了
            PPartChainDetailsMap.put(pPartChainDetail.getPartId(),pPartChainDetail);
            return pPartChainDetail;//判断下一个
        }
        //4.判断pid==ppid?
        if(pPartChainDetail.getPartId().equals(pPartChainDetail.getParentPartId())){//说明是根节点
//            Integer order = depositMap.get(0);//根节点的深度为0,所以可以以此取出深度为0的排序
//            if(order==null){
//                order=0;
//            }
            pPartChainDetail.setChainDepth(0);
            Integer rootOrder=0;
            pPartChainDetail.setChainIndex(rootOrder);//根节点的index永远是0
            pPartChainDetail.setFlag(true);
//            pPartChainDetail.setChildrenOrder(0);//其下的子节点排序从1开始计数
            rootOrder++;//增1
//            rootOrderMap.put(pPartChainDetail.getPartId(),rootOrder);//最上面已经添加了..防止根节点在子节点下面的情况
//            depositMap.put(0,order);//放入map中
//            PPartChainDetailsMap.put(pPartChainDetail.getPartId(),pPartChainDetail);
            return pPartChainDetail;//判断下一个
        }else{//说明不是根节点
            //拿到它的父节点
            PPartChainDetails parentPartChainDetails = PPartChainDetailsMap.get(pPartChainDetail.getParentPartId());
            //判断父节点是否已经检查了?
            if(parentPartChainDetails.getFlag()!=null&&parentPartChainDetails.getFlag()){//已经检查过了
                if(parentPartChainDetails.getChainDepth().equals(-1)&&parentPartChainDetails.getChainIndex().equals(-1)){
                    //说明上层父节点是无效的..所以当前节点也是无效的
                    pPartChainDetail.setChainDepth(-1);
                    pPartChainDetail.setChainIndex(-1);
                    pPartChainDetail.setFlag(true);
                    PPartChainDetailsMap.put(pPartChainDetail.getPartId(),pPartChainDetail);
                    return pPartChainDetail;
                }
                //直接拿来用
                Integer chainDepth = parentPartChainDetails.getChainDepth()+1;//父的深度+1就是当前节点的深度值
//                Integer order = depositMap.get(chainDepth);
                //获取当前元素的根节点
                PPartChainDetails root = getRoot(parentPartChainDetails);
                Integer childrenOrder = rootOrderMap.get(root.getPartId());
//                if(childrenOrder==null){
//                    childrenOrder=0;
//                }
                pPartChainDetail.setChainDepth(chainDepth);
                pPartChainDetail.setChainIndex(childrenOrder);
                childrenOrder++;
//                depositMap.put(chainDepth,order);//放入map中
//                parentPartChainDetails.setChildrenOrder(childrenOrder);
                rootOrderMap.put(parentPartChainDetails.getPartId(),childrenOrder);
                PPartChainDetailsMap.put(pPartChainDetail.getPartId(),pPartChainDetail);
//                PPartChainDetailsMap.put(parentPartChainDetails.getPartId(),parentPartChainDetails);//父节点的childrenOrder更新了,所以也要重新覆盖
                return pPartChainDetail;//判断下一个
            }else{//还未检查,要先检查父节点的后拿到父节点的再进行处理
                parentPartChainDetails = handle(parentPartChainDetails);//递归
                if(parentPartChainDetails.getChainDepth().equals(-1)&&parentPartChainDetails.getChainIndex().equals(-1)){
                    //说明上层父节点是无效的..所以当前节点也是无效的
                    pPartChainDetail.setChainDepth(-1);
                    pPartChainDetail.setChainIndex(-1);
                    pPartChainDetail.setFlag(true);
                    PPartChainDetailsMap.put(pPartChainDetail.getPartId(),pPartChainDetail);
                    return pPartChainDetail;
                }
                //直接拿来用
                Integer chainDepth = parentPartChainDetails.getChainDepth()+1;//父的深度+1就是当前节点的深度值
//                Integer order = depositMap.get(chainDepth);
                //获取当前元素的根节点
                PPartChainDetails root = getRoot(parentPartChainDetails);
                Integer childrenOrder = rootOrderMap.get(root.getPartId());
//                if(childrenOrder==null){
//                    childrenOrder=0;
//                }
                pPartChainDetail.setChainDepth(chainDepth);
                pPartChainDetail.setChainIndex(childrenOrder);
                childrenOrder++;
//                depositMap.put(chainDepth,order);//放入map中
//                parentPartChainDetails.setChildrenOrder(childrenOrder);
                rootOrderMap.put(parentPartChainDetails.getPartId(),childrenOrder);
                PPartChainDetailsMap.put(pPartChainDetail.getPartId(),pPartChainDetail);
//                PPartChainDetailsMap.put(parentPartChainDetails.getPartId(),parentPartChainDetails);//父节点的childrenOrder更新了,所以也要重新覆盖
                return pPartChainDetail;//判断下一个
            }
        }
    }

    //获取顶级节点
    private PPartChainDetails getRoot(PPartChainDetails pPartChainDetails){
        if(pPartChainDetails==null){
            return null;
        }

        if (pPartChainDetails.getPartId().equals(pPartChainDetails.getParentPartId())) {
            //根节点
            return pPartChainDetails;
        }
        PPartChainDetails parentPartChainDetails = PPartChainDetailsMap.get(pPartChainDetails.getParentPartId());
        PPartChainDetails root = getRoot(parentPartChainDetails);
        return root;
    }

}
