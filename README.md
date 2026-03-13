
./process.sh \
  -i "/home/xiaobin/ubis/dataset/sift/head_index" \
  -d 128 \
  -q "/home/xiaobin/ubis/dataset/sift/batches/query_embeddings10" \
  -r 10 \
  -U "/home/xiaobin/ubis/UBIS/Release/ubis"


./indexsearcher   -i /home/xiaobin/ubis/dataset/sift/batches/query_embeddings0   -x /home/xiaobin/ubis/dataset/sift   -d 128   -v float   -f DEFAULT   -k 10   -b 10000   -o result.txt -t 1 -r /home/xiaobin/ubis/dataset/sift/truths/truth_embeddings_0

./indexsearcher   -i /home/xiaobin/ubis/dataset/sift/batches/query_embeddings0   -x /home/xiaobin/ubis/dataset/sift   -d 128   -v float   -f DEFAULT   -k 10   -b 10000   -o result.txt -t 1 -r /home/xiaobin/ubis/dataset/sift/truths/truth_embeddings_0



./indexsearcher   -i /home/xiaobin/ubis/dataset/sift/batches/query_embeddings0   -x /home/xiaobin/ubis/dataset/sift   -d 128   -v float   -f DEFAULT   -k 10   -b 10000   -o result.txt -t 1 -r /home/xiaobin/ubis/dataset/sift/truths/truth_embeddings_0

[1] Using AVX512 InstructionSet!
[1] folderPath: /home/xiaobin/ubis/dataset/sift/ 
[1] [LoadIndex] opening config: /home/xiaobin/ubis/dataset/sift/indexloader.ini
[1] [LoadIndex] config loaded successfully
[1] [LoadIndex] IndexAlgoType=2 (0=BKT,2=KDT,3=SPANN), ValueType=3 -> LoadIndexConfig will dispatch to corresponding LoadConfig
[1] [LoadIndexConfig] called (then LoadConfig); only SPANN index parses [BuildSSDIndex] FullVectorPath -> p_opts.m_fullVectorPath
[1] [LoadConfig] called
[1] [Config] Base.deletedids = /home/xiaobin/ubis/dataset/sift/head_index/VectorVersionLabel.bin
[1] Setting deletedids = DeletedIDs with value /home/xiaobin/ubis/dataset/sift/head_index/VectorVersionLabel.bin
[1] [Config] Base.dim = 128
[1] Setting dim = Dim with value 128
[1] [Config] Base.distcalcmethod = L2
[1] Setting distcalcmethod = DistCalcMethod with value L2
[1] [Config] Base.generatetruth = false
[1] Setting generatetruth = GenerateTruth with value false
[1] [Config] Base.headindexfolder = head_index
[1] Setting headindexfolder = HeadIndexFolder with value head_index
[1] [Config] Base.headvectorids = head_vectors_ID_UInt8_L2_base_DEFUALT.bin
[1] Setting headvectorids = HeadVectorIDs with value head_vectors_ID_UInt8_L2_base_DEFUALT.bin
[1] [Config] Base.headvectors = head_vectors_UInt8_L2_base_DEFUALT.bin
[1] Setting headvectors = HeadVectors with value head_vectors_UInt8_L2_base_DEFUALT.bin
[1] [Config] Base.indexalgotype = BKT
[1] Setting indexalgotype = IndexAlgoType with value BKT
[1] [Config] Base.indexdirectory = /home/xiaobin/ubis/dataset/sift/head_index
[1] Setting indexdirectory = IndexDirectory with value /home/xiaobin/ubis/dataset/sift/head_index
[1] [Config] Base.postingversionlabelfile = /home/xiaobin/ubis/dataset/sift/head_index/PostingVersionLabel.bin
[1] Setting postingversionlabelfile = PostingVersionLabelFile with value /home/xiaobin/ubis/dataset/sift/head_index/PostingVersionLabel.bin
[1] [Config] Base.querydelimiter = 
[1] Setting querydelimiter = QueryDelimiter with value 
[1] [Config] Base.querypath = /home/xiaobin/ubis/dataset/sift/batches/query_embeddings10
[1] Setting querypath = QueryPath with value /home/xiaobin/ubis/dataset/sift/batches/query_embeddings10
[1] [Config] Base.querysize = 11
[1] Setting querysize = QuerySize with value 11
[1] [Config] Base.querytype = DEFAULT
[1] Setting querytype = QueryType with value DEFAULT
[1] [Config] Base.truthpath = /home/xiaobin/ubis/dataset/sift/batches/base_embeddings_truth10
[1] Setting truthpath = TruthPath with value /home/xiaobin/ubis/dataset/sift/batches/base_embeddings_truth10
[1] [Config] Base.truthtype = DEFAULT
[1] Setting truthtype = TruthType with value DEFAULT
[1] [Config] Base.valuetype = Float
[1] Setting valuetype = ValueType with value Float
[1] [Config] Base.vectordelimiter = 
[1] Setting vectordelimiter = VectorDelimiter with value 
[1] [Config] Base.vectorpath = /home/xiaobin/ubis/dataset/sift/batches/base_embeddings10,/home/xiaobin/ubis/dataset/sift/batches/base_embeddings10_metadata.bin,/home/xiaobin/ubis/dataset/sift/batches/base_embeddings10_metadataindex.bin
[1] Setting vectorpath = VectorPath with value /home/xiaobin/ubis/dataset/sift/batches/base_embeddings10,/home/xiaobin/ubis/dataset/sift/batches/base_embeddings10_metadata.bin,/home/xiaobin/ubis/dataset/sift/batches/base_embeddings10_metadataindex.bin
[1] [Config] Base.vectorsize = 1081
[1] Setting vectorsize = VectorSize with value 1081
[1] [Config] Base.vectortype = DEFAULT
[1] Setting vectortype = VectorType with value DEFAULT
[1] [Config] Base.warmupdelimiter = 
[1] Setting warmupdelimiter = WarmupDelimiter with value 
[1] [Config] Base.warmuppath = 
[1] Setting warmuppath = WarmupPath with value 
[1] [Config] Base.warmupsize = 29316
[1] Setting warmupsize = WarmupSize with value 29316
[1] [Config] Base.warmuptype = DEFAULT
[1] Setting warmuptype = WarmupType with value DEFAULT
[1] [Config] SelectHead.analyzeonly = false
[1] Setting AnalyzeOnly with value false
[1] [Config] SelectHead.bktkmeansk = 32
[1] Setting BKTKmeansK with value 32
[1] [Config] SelectHead.bktleafsize = 8
[1] Setting BKTLeafSize with value 8
[1] [Config] SelectHead.calcstd = true
[1] Setting CalcStd with value true
[1] [Config] SelectHead.isexecute = false
[1] Setting isExecute with value false
[1] [Config] SelectHead.nooutput = false
[1] Setting NoOutput with value false
[1] [Config] SelectHead.numberofthreads = 10
[1] Setting NumberOfThreads with value 10
[1] [Config] SelectHead.printsizecount = true
[1] Setting PrintSizeCount with value true
[1] [Config] SelectHead.ratio = 0.02
[1] Setting Ratio with value 0.02
[1] [Config] SelectHead.recursivechecksmallcluster = true
[1] Setting RecursiveCheckSmallCluster with value true
[1] [Config] SelectHead.samplesnumber = 1000
[1] Setting SamplesNumber with value 1000
[1] [Config] SelectHead.savebkt = false
[1] Setting SaveBKT with value false
[1] [Config] SelectHead.selectdynamically = true
[1] Setting SelectDynamically with value true
[1] [Config] SelectHead.selectthreshold = 50
[1] Setting SelectThreshold with value 50
[1] [Config] SelectHead.splitfactor = 40
[1] Setting SplitFactor with value 40
[1] [Config] SelectHead.splitthreshold = 80
[1] Setting SplitThreshold with value 80
[1] [Config] SelectHead.treenumber = 1
[1] Setting TreeNumber with value 1
[1] [Config] BuildHead.addcef = 500
[1] Setting AddCEF with value 500
[1] [Config] BuildHead.addcountforrebuild = 1000
[1] Setting AddCountForRebuild with value 1000
[1] [Config] BuildHead.bktkmeansk = 32
[1] Setting BKTKmeansK with value 32
[1] [Config] BuildHead.bktlambdafactor = 100.000000
[1] Setting BKTLambdaFactor with value 100.000000
[1] [Config] BuildHead.bktleafsize = 8
[1] Setting BKTLeafSize with value 8
[1] [Config] BuildHead.bktnumber = 1
[1] Setting BKTNumber with value 1
[1] [Config] BuildHead.cef = 1000
[1] Setting CEF with value 1000
[1] [Config] BuildHead.datablocksize = 1048576
[1] Setting DataBlockSize with value 1048576
[1] [Config] BuildHead.datacapacity = 2147483647
[1] Setting DataCapacity with value 2147483647
[1] [Config] BuildHead.deletepercentageforrefine = 0.400000
[1] Setting DeletePercentageForRefine with value 0.400000
[1] [Config] BuildHead.deletevectorfilepath = deletes.bin
[1] Setting DeleteVectorFilePath with value deletes.bin
[1] [Config] BuildHead.distcalcmethod = L2
[1] Setting DistCalcMethod with value L2
[1] [Config] BuildHead.enablebfs = 0
[1] Setting EnableBfs with value 0
[1] [Config] BuildHead.enablerebuild = 1
[1] Setting EnableRebuild with value 1
[1] [Config] BuildHead.gpugraphtype = 2
[1] Setting GPUGraphType with value 2
[1] [Config] BuildHead.gpuleafsize = 500
[1] Setting GPULeafSize with value 500
[1] [Config] BuildHead.gpurefinedepth = 30
[1] Setting GPURefineDepth with value 30
[1] [Config] BuildHead.gpurefinesteps = 0
[1] Setting GPURefineSteps with value 0
[1] [Config] BuildHead.graphcefscale = 2.000000
[1] Setting GraphCEFScale with value 2.000000
[1] [Config] BuildHead.graphfilepath = graph.bin
[1] Setting GraphFilePath with value graph.bin
[1] [Config] BuildHead.graphneighborhoodscale = 2.000000
[1] Setting GraphNeighborhoodScale with value 2.000000
[1] [Config] BuildHead.hashtableexponent = 2
[1] Setting HashTableExponent with value 2
[1] [Config] BuildHead.headnumgpus = 1
[1] Setting HeadNumGPUs with value 1
[1] [Config] BuildHead.isexecute = false
[1] Setting isExecute with value false
[1] [Config] BuildHead.maxcheck = 4096
[1] Setting MaxCheck with value 4096
[1] [Config] BuildHead.maxcheckforrefinegraph = 8192
[1] Setting MaxCheckForRefineGraph with value 8192
[1] [Config] BuildHead.metarecordsize = 10
[1] Setting MetaRecordSize with value 10
[1] [Config] BuildHead.neighborhoodsize = 32
[1] Setting NeighborhoodSize with value 32
[1] [Config] BuildHead.numberofinitialdynamicpivots = 50
[1] Setting NumberOfInitialDynamicPivots with value 50
[1] [Config] BuildHead.numberofotherdynamicpivots = 4
[1] Setting NumberOfOtherDynamicPivots with value 4
[1] [Config] BuildHead.numberofthreads = 80
[1] Setting NumberOfThreads with value 80
[1] [Config] BuildHead.numtopdimensiontptreesplit = 5
[1] Setting NumTopDimensionTpTreeSplit with value 5
[1] [Config] BuildHead.refineiterations = 2
[1] Setting RefineIterations with value 2
[1] [Config] BuildHead.rngfactor = 1.000000
[1] Setting RNGFactor with value 1.000000
[1] [Config] BuildHead.samples = 1000
[1] Setting Samples with value 1000
[1] [Config] BuildHead.thresholdofnumberofcontinuousnobetterpropagation = 3
[1] Setting ThresholdOfNumberOfContinuousNoBetterPropagation with value 3
[1] [Config] BuildHead.tptbalancefactor = 2
[1] Setting TPTBalanceFactor with value 2
[1] [Config] BuildHead.tptleafsize = 2000
[1] Setting TPTLeafSize with value 2000
[1] [Config] BuildHead.tptnumber = 32
[1] Setting TPTNumber with value 32
[1] [Config] BuildHead.treefilepath = tree.bin
[1] Setting TreeFilePath with value tree.bin
[1] [Config] BuildHead.vectorfilepath = vectors.bin
[1] Setting VectorFilePath with value vectors.bin
[1] [Config] BuildSSDIndex.appendthreadnum = 4
[1] Setting AppendThreadNum with value 4
[1] [Config] BuildSSDIndex.balancefactor = 0.15
[1] Setting BalanceFactor with value 0.15
[1] [Config] BuildSSDIndex.basevectorsplitpath = /home/xiaobin/ubis/dataset/sift/query_vector_range.bin
[1] Setting BaseVectorSplitPath with value /home/xiaobin/ubis/dataset/sift/query_vector_range.bin
[1] [Config] BuildSSDIndex.buildssdindex = false
[1] Setting BuildSsdIndex with value false
[1] [Config] BuildSSDIndex.caltruth = true
[1] Setting CalTruth with value true
[1] [Config] BuildSSDIndex.days = 0
[1] Setting Days with value 0
[1] [Config] BuildSSDIndex.deleteqps = 16000
[1] Setting DeleteQPS with value 16000
[1] [Config] BuildSSDIndex.disablereassign = false
[1] Setting DisableReassign with value false
[1] [Config] BuildSSDIndex.enablefinedlock = true
[1] Setting EnableFinedLock with value true
[1] [Config] BuildSSDIndex.enableoptsplit = true
[1] Setting EnableOptSplit with value true
[1] [Config] BuildSSDIndex.excludehead = false
[1] Setting ExcludeHead with value false
[1] [Config] BuildSSDIndex.filterappend = true
[1] Setting FilterAppend with value true
[1] [Config] BuildSSDIndex.fullvectorpath = /home/xiaobin/ubis/dataset/sift/batches/base_embeddings10
[1] Setting FullVectorPath with value /home/xiaobin/ubis/dataset/sift/batches/base_embeddings10
[1] [Config] BuildSSDIndex.insertthreadnum = 1
[1] Setting InsertThreadNum with value 1
[1] [Config] BuildSSDIndex.internalresultnum = 64
[1] Setting InternalResultNum with value 64
[1] [Config] BuildSSDIndex.isexecute = true
[1] Setting isExecute with value true
[1] [Config] BuildSSDIndex.kvpath = /home/xiaobin/ubis/dataset/sift/rocksdb
[1] Setting KVPath with value /home/xiaobin/ubis/dataset/sift/rocksdb
[1] [Config] BuildSSDIndex.latencylimit = 9.0
[1] Setting LatencyLimit with value 9.0
[1] [Config] BuildSSDIndex.loadallvectors = true
[1] Setting LoadAllVectors with value true
[1] [Config] BuildSSDIndex.maxdistratio = 1000000
[1] Setting MaxDistRatio with value 1000000
[1] [Config] BuildSSDIndex.maxinternalresultnum = 16
[1] Setting MaxInternalResultNum with value 16
[1] [Config] BuildSSDIndex.mergethreshold = 10
[1] Setting MergeThreshold with value 10
[1] [Config] BuildSSDIndex.mininternalresultnum = 16
[1] Setting MinInternalResultNum with value 16
[1] [Config] BuildSSDIndex.numberofthreads = 10
[1] Setting NumberOfThreads with value 10
[1] [Config] BuildSSDIndex.onlysearchfinalbatch = true
[1] Setting OnlySearchFinalBatch with value true
[1] [Config] BuildSSDIndex.outputemptyreplicaid = 1
[1] Setting OutputEmptyReplicaID with value 1
[1] [Config] BuildSSDIndex.postingpagelimit = 10
[1] Setting PostingPageLimit with value 10
[1] [Config] BuildSSDIndex.reassignk = 0
[1] Setting ReassignK with value 0
[1] [Config] BuildSSDIndex.reassignthreadnum = 0
[1] Setting ReassignThreadNum with value 0
[1] [Config] BuildSSDIndex.replicacount = 8
[1] Setting ReplicaCount with value 8
[1] [Config] BuildSSDIndex.resultnum = 10
[1] Setting ResultNum with value 10
[1] [Config] BuildSSDIndex.sampling = 2
[1] Setting Sampling with value 2
[1] [Config] BuildSSDIndex.searchduringupdate = true
[1] Setting SearchDuringUpdate with value true
[1] [Config] BuildSSDIndex.searchinternalresultnum = 32
[1] Setting SearchInternalResultNum with value 32
[1] [Config] BuildSSDIndex.searchpostingpagelimit = 10
[1] Setting SearchPostingPageLimit with value 10
[1] [Config] BuildSSDIndex.searchthreadnum = 4
[1] Setting SearchThreadNum with value 4
[1] [Config] BuildSSDIndex.searchtimes = 1
[1] Setting SearchTimes with value 1
[1] [Config] BuildSSDIndex.searchwaitmillitime = 2000
[1] Setting SearchWaitMilliTime with value 2000
[1] [Config] BuildSSDIndex.showupdateprogress = false
[1] Setting ShowUpdateProgress with value false
[1] [Config] BuildSSDIndex.smallesttimeunit = 1000
[1] Setting SmallestTimeUnit with value 1000
[1] [Config] BuildSSDIndex.spdkbatchsize = 64
[1] Setting SpdkBatchSize with value 64
[1] [Config] BuildSSDIndex.ssdinfofile = /home/xiaobin/ubis/dataset/sift/head_index/SsdInfoFile.bin
[1] Setting SsdInfoFile with value /home/xiaobin/ubis/dataset/sift/head_index/SsdInfoFile.bin
[1] [Config] BuildSSDIndex.startnum = 100
[1] Setting StartNum with value 100
[1] [Config] BuildSSDIndex.step = 100
[1] Setting Step with value 100
[1] [Config] BuildSSDIndex.stepinternalresultnum = 16
[1] Setting StepInternalResultNum with value 16
[1] [Config] BuildSSDIndex.streamingupdate = true
[1] Setting StreamingUpdate with value true
[1] [Config] BuildSSDIndex.ternimatenum = 1000
[1] Setting TernimateNum with value 1000
[1] [Config] BuildSSDIndex.truthfileprefix = /home/xiaobin/ubis/dataset/sift/truths/truth_embeddings
[1] Setting TruthFilePrefix with value /home/xiaobin/ubis/dataset/sift/truths/truth_embeddings
[1] [Config] BuildSSDIndex.update = false
[1] Setting Update with value false
[1] [Config] BuildSSDIndex.usedirectio = true
[1] Setting UseDirectIO with value true
[1] [Config] BuildSSDIndex.usekv = true
[1] Setting UseKV with value true
[1] [Config] BuildSSDIndex.usespdk = false
[1] Setting UseSPDK with value false
[1] [LoadConfig] return Success
[1] Load Vector (27,128) Finish!
[1] Load BKT (1,29) Finish!
[1] Load RNG (27,32) Finish!
[1] Load DeleteID (27,1) Finish!
[1] Setting NumberOfThreads with value 10
[1] Setting MaxCheck with value 4096
[1] Setting HashTableExponent with value 4
[1] SPFresh: New Rocksdb: /home/xiaobin/ubis/dataset/sift/rocksdb
[1] Posting size limit: 79, search limit: 9.000000, merge threshold: 10
[1] DataBlockSize: 1048576, Capacity: 2147483647
[1] Load versionLabelID From /home/xiaobin/ubis/dataset/sift/head_index/VectorVersionLabel.bin
[1] Load versionLabelID (1092,1) Finish!
[1] Load postingVersionLabelID From /home/xiaobin/ubis/dataset/sift/head_index/PostingVersionLabel.bin
[1] Load postingVersionLabelID (27,1) Finish!
[1] Load PostingSizeRecord From /home/xiaobin/ubis/dataset/sift/head_index/SsdInfoFile.bin
[1] Load PostingSizeRecord (27,1) Finish!
[1] Current vector num: 1092.
[1] Current posting num: (27,27).
[1] current memory vector num:2072
[1] [Config] Base.numberofthreads = 1
[1] [Config] SelectHead.numberofthreads = 1
[1] Setting NumberOfThreads with value 1
[1] [Config] BuildHead.numberofthreads = 1
[1] Setting NumberOfThreads with value 1
[1] [Config] BuildSSDIndex.numberofthreads = 1
[1] Setting NumberOfThreads with value 1
[1] [Config] Index.numberofthreads = 1
[1] Setting NumberOfThreads with value 1
[1] Load Vector(1,128)
[1] Load binary truth...
[1] [query]             [maxcheck]      [avg]   [99%]   [95%]    [recall]        [qps]   [mem]
[1] p_iTruthNumber:1, originalK:10
[1] [Config] .MaxCheck = 8192
[1] [SearchIndex] after head: GetResultNum=32
[1] [SearchIndex] head[0] VID=11 Dist=119525.000000
[1] [SearchIndex] head[1] VID=5 Dist=122188.000000
[1] [SearchIndex] head[2] VID=9 Dist=124210.000000
[1] [SearchIndex] head[3] VID=3 Dist=139381.000000
[1] [SearchIndex] head[4] VID=8 Dist=173521.000000
[1] [SearchIndex] limitDist=119524999168.000000 (pFirst->Dist=119525.000000, maxDistRatio=1000000.00)
[1] SearchIndex: head result invalid (res null or VID=-1) at position 27, skip disk search for remaining.
[1] [SearchIndex] postingIDs.size()=0 (passed to disk search)
[1] SearchIndex: p_stats is null, skip exSetUpLatency.ExtraDynamicSearcher.h-2646
[1] SearchIndex: p_stats is null, use remainLimit = m_hardLatencyLimit.ExtraDynamicSearcher.h-2660
[1] [ExtraDynamicSearcher] filtered_postingIDs.size()=0
[1] [ExtraDynamicSearcher] postingLists.size()=0
[1] [SearchIndex] after disk: GetResultNum=32
[1] [SearchIndex] result[0] VID=-1 Dist=34028234663852885981170418348451692544.000000
[1] [SearchIndex] result[1] VID=-1 Dist=34028234663852885981170418348451692544.000000
[1] [SearchIndex] result[2] VID=-1 Dist=34028234663852885981170418348451692544.000000
[1] [SearchIndex] result[3] VID=-1 Dist=34028234663852885981170418348451692544.000000
[1] [SearchIndex] result[4] VID=-1 Dist=34028234663852885981170418348451692544.000000
[1] [Query 0] query_id=0 vector(first 4 dims): 1.000000 3.000000 11.000000 110.000000 ...
[1] [Query 0] results(10): (NULL,34028234663852885981170418348451692544.000000) (NULL,34028234663852885981170418348451692544.000000) (NULL,34028234663852885981170418348451692544.000000) (NULL,34028234663852885981170418348451692544.000000) (NULL,34028234663852885981170418348451692544.000000) (NULL,34028234663852885981170418348451692544.000000) (NULL,34028234663852885981170418348451692544.000000) (NULL,34028234663852885981170418348451692544.000000) (NULL,34028234663852885981170418348451692544.000000) (NULL,34028234663852885981170418348451692544.000000)
[1] 0-1 8192    0.0069  0.0069  0.0069  0.0000          115.2605       0GB
[1] 0-1 8192    0.0069  0.0069  0.0069  0.0000  115.2605
[1] Output results finish!